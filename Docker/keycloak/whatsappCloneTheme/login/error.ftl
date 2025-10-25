<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WhatsApp</title>
    <link rel="icon" type="image/png"
          href="${url.resourcesPath!''}/img/favicon.ico">
    <link rel="stylesheet"
          href="${url.resourcesPath!''}/css/style.css">
</head>
<body>
    <div class="background">
        <div class="logo">
            <img src="${url.resourcesPath!''}/img/logo.png"
                 class="logo-img"
                 alt="WhatsApp Clone Logo">
            <div class="logo-text">WhatsApp Clone App</div>
        </div>

        <div class="whatsapp-container">
            <div class="card">
                <h2>Oops! Something went wrong</h2>
                <p>Please try again or contact support if the issue persists.</p>

                <#if message?? && message.summary??>
                    <div class="message error">
                        ${message.summary?no_esc}
                        <#if message.detail??>
                            <div class="message-detail">
                                ${message.detail?no_esc}
                            </div>
                        </#if>
                    </div>
                <#else>
                    <div class="message error">
                        An unexpected error occurred. Please try again later.
                    </div>
                </#if>

                <hr class="separator" />

                <div class="links">
                    <a href="${url.loginUrl!'#'}"
                       id="back-to-login"
                       class="button-link">
                        Back to Login
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
