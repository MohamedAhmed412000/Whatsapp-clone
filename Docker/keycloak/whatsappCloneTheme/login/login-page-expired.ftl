<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WhatsApp</title>
    <link rel="icon" type="image/png" href="${url.resourcesPath}/img/favicon.ico">
    <link rel="stylesheet" href="${url.resourcesPath}/css/style.css">
</head>
<body>
    <div class="background">
        <div class="logo">
            <img src="${url.resourcesPath}/img/logo.png" class="logo-img">
            <div class="logo-text">WhatsApp Clone App</div>
        </div>

        <div class="whatsapp-container">
            <div class="card">
                <h2>Session Expired</h2>

                <div class="message error">
                    Your login session has expired or the link you used is no longer valid.<br><br>
                    Please return to the login page and try again.
                </div>

                <hr class="separator" />

                <div class="links">
                    <a id="back-to-login" href="${url.loginRestartFlowUrl}">Return to login</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
