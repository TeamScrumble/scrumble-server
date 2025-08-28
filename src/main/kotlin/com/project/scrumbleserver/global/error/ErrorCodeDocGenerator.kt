package com.project.scrumbleserver.global.error

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

@Component
class ErrorCodeDocGenerator(
    private val errorCodeCollector: ErrorCodeCollector,
) {
    companion object {
        private val OUTPUT_PATH = Paths.get("src/main/resources/static/error-codes.html")
    }

    @PostConstruct
    operator fun invoke() {
        val errorCodes = errorCodeCollector.collect()

        if (Files.exists(OUTPUT_PATH)) {
            Files.delete(OUTPUT_PATH)
        }

        val html = buildHtml(errorCodes)

        Files.createDirectories(OUTPUT_PATH.parent)
        Files.writeString(OUTPUT_PATH, html)

        logger.info { "ErrorCodeDocument generated at: $OUTPUT_PATH" }
        logger.info { "ErrorCodeDocument local link: http://localhost:8080/error-codes.html" }
    }

    private fun buildHtml(errorCodes: Map<String, List<ErrorCode>>): String = buildString {
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        appendLine("<!DOCTYPE html>")
        appendLine("<html lang='ko'>")
        appendLine("<head>")
        appendLine("<meta charset='UTF-8'/>")
        appendLine("<meta name='viewport' content='width=device-width, initial-scale=1'/>")
        appendLine("<title>Error Codes</title>")
        appendLine(
            """
            <style>
              :root{
                --bg:#ffffff; --fg:#111827; --muted:#6b7280; --card:#ffffff; --border:#e5e7eb; --accent:#f3f4f6; --link:#2563eb;
                --ok:#10b981; --warn:#f59e0b; --err:#ef4444;
              }
              @media (prefers-color-scheme: dark){
                :root{
                  --bg:#0b0f14; --fg:#e5e7eb; --muted:#9ca3af; --card:#0f141b; --border:#1f2937; --accent:#111827; --link:#60a5fa;
                }
                ::selection{background:#1f2937;}
              }
              *{box-sizing:border-box}
              html,body{height:100%}
              body{margin:0;background:var(--bg);color:var(--fg);font:14px/1.6 ui-sans-serif,system-ui,AppleSDGothicNeo,Segoe UI,Roboto,Arial}
              a{color:var(--link);text-decoration:none}
              .container{max-width:1100px;margin:0 auto;padding:24px}
              .header{position:sticky;top:0;z-index:5;backdrop-filter:saturate(180%) blur(8px);background:color-mix(in srgb,var(--bg),transparent 10%);border-bottom:1px solid var(--border)}
              .header-inner{display:flex;gap:16px;align-items:center;justify-content:space-between;padding:12px 24px}
              .title{font-size:18px;font-weight:700;margin:0}
              .meta{font-size:12px;color:var(--muted)}
              .search{display:flex;gap:8px;align-items:center}
              .search input{
                width:280px;max-width:60vw;padding:10px 12px;border:1px solid var(--border);border-radius:10px;background:var(--card);color:var(--fg);
                outline:none;
              }
              .search input::placeholder{color:var(--muted)}
              .btn{
                padding:10px 12px;border:1px solid var(--border);background:var(--card);color:var(--fg);border-radius:10px;cursor:pointer
              }
              .grid{display:grid;grid-template-columns:260px 1fr;gap:24px;margin-top:16px}
              @media (max-width:960px){.grid{grid-template-columns:1fr}}
              .toc{position:sticky;top:62px;border:1px solid var(--border);border-radius:12px;background:var(--card);padding:12px}
              .toc h3{margin:6px 8px 10px 8px;font-size:13px;color:var(--muted)}
              .toc a{display:block;padding:8px 10px;border-radius:8px;color:var(--fg)}
              .toc a:hover{background:var(--accent)}
              .card{border:1px solid var(--border);background:var(--card);border-radius:14px;overflow:hidden}
              .card-header{display:flex;align-items:center;justify-content:space-between;padding:14px 16px;border-bottom:1px solid var(--border)}
              .card-title{font-size:16px;font-weight:700;margin:0}
              .card-sub{font-size:12px;color:var(--muted)}
              .card-tools{display:flex;gap:8px}
              .card-content{padding:0}
              table{width:100%;border-collapse:collapse}
              thead th{
                position:sticky;top:60px;background:var(--accent);color:var(--fg);text-align:left;font-size:12px;letter-spacing:.02em;border-bottom:1px solid var(--border);padding:10px
              }
              tbody td{border-top:1px solid var(--border);padding:10px;vertical-align:top}
              tbody tr:hover{background:color-mix(in srgb,var(--accent),transparent 30%)}
              .pill{display:inline-block;padding:.15rem .5rem;border:1px solid var(--border);border-radius:999px;font-size:12px;color:var(--muted)}
              .foot{margin:24px 0 40px;color:var(--muted);font-size:12px}
              .hide{display:none!important}
            </style>
            """.trimIndent()
        )
        appendLine("</head>")
        appendLine("<body>")
        appendLine("<header class='header'><div class='header-inner'>")
        appendLine("<div>")
        appendLine("<h1 class='title'>Error Codes</h1>")
        appendLine("<div class='meta'>Generated at <strong>$now</strong></div>")
        appendLine("</div>")
        appendLine("<div class='search'>")
        appendLine("<input id='q' type='search' placeholder='검색: code / description / enum name' aria-label='검색'/>")
        appendLine("<button id='toggle-all' class='btn' type='button'>모두 접기</button>")
        appendLine("</div>")
        appendLine("</div></header>")

        appendLine("<main class='container'>")
        appendLine("<div class='grid'>")

        // 좌측 TOC
        appendLine("<nav class='toc'><h3>Enums</h3>")
        errorCodes.keys.sorted().forEach { enumName ->
            appendLine("<a href='#$enumName'>$enumName</a>")
        }
        appendLine("</nav>")

        // 본문
        appendLine("<section>")
        errorCodes.toSortedMap().forEach { (enumName, codes) ->
            val count = codes.size
            appendLine("<article class='card' id='$enumName'>")
            appendLine("<div class='card-header'>")
            appendLine("<div><h2 class='card-title'>$enumName</h2><div class='card-sub'><span class='pill'>$count items</span></div></div>")
            appendLine("<div class='card-tools'>")
            appendLine("<button class='btn btn-collapse' data-target='$enumName' type='button'>접기</button>")
            appendLine("</div></div>")
            appendLine("<div class='card-content' data-section='$enumName'>")
            appendLine("<table><thead><tr><th style='width:220px'>Code</th><th>Description</th></tr></thead><tbody>")
            codes.sortedBy { it.code }.forEach { ec ->
                appendLine("<tr data-enum='$enumName'><td><code>${escape(ec.code)}</code></td><td>${escape(ec.description)}</td></tr>")
            }
            appendLine("</tbody></table></div></article><br/>")
        }
        appendLine("<p class='foot'>* 이 문서는 애플리케이션 기동 시 자동 생성되었습니다.</p>")
        appendLine("</section></div></main>")

        // JS (템플릿 리터럴 $ 이스케이프 주의)
        appendLine(
            """
            <script>
              const q = document.getElementById('q');
              const toggleAllBtn = document.getElementById('toggle-all');
              const sections = [...document.querySelectorAll('[data-section]')];
              const collapseBtns = [...document.querySelectorAll('.btn-collapse')];

              let allCollapsed = false;

              function filterRows(term){
                const t = term.trim().toLowerCase();
                const rows = [...document.querySelectorAll('tbody tr')];
                rows.forEach(r=>{
                  const enumName = (r.getAttribute('data-enum')||'').toLowerCase();
                  const text = r.textContent.toLowerCase();
                  const show = !t || text.includes(t) || enumName.includes(t);
                  r.classList.toggle('hide', !show);
                });
                sections.forEach(s=>{
                  const anyVisible = [...s.querySelectorAll('tbody tr')].some(r=>!r.classList.contains('hide'));
                  s.parentElement.classList.toggle('hide', !anyVisible);
                });
              }

              q?.addEventListener('input', e=>filterRows(e.target.value));

              collapseBtns.forEach(btn=>{
                btn.addEventListener('click',()=>{
                  const id = btn.getAttribute('data-target');
                  const sec = document.querySelector(`[data-section="${'$'}{id}"]`); // Changed
                  const nowHidden = sec.classList.toggle('hide');
                  btn.textContent = nowHidden ? '펼치기' : '접기';
                });
              });

              toggleAllBtn?.addEventListener('click',()=>{
                allCollapsed = !allCollapsed;
                sections.forEach(s=>s.classList.toggle('hide', allCollapsed));
                collapseBtns.forEach(b=>b.textContent = allCollapsed ? '펼치기' : '접기');
                toggleAllBtn.textContent = allCollapsed ? '모두 펼치기' : '모두 접기';
              });
            </script>
            """.trimIndent()
        )

        appendLine("</body></html>")
    }

    private fun escape(s: String?) =
        (s ?: "").replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}