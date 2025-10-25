<#macro emailLayout>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${msg("emailSubject")?default("WhatsApp Clone Notification")}</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #202C33;
      margin: 0;
      padding: 0;
    }
    .email-container {
      max-width: 600px;
      background-color: #272b2b;
      margin: 30px auto;
      border-radius: 10px;
      overflow: hidden;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    .email-header {
      background-color: #00A884;
      text-align: center;
      padding: 20px;
    }
    .logo {
      width: 60px;
      height: 60px;
    }
    .email-body {
      padding: 30px;
      color: #657580;
      line-height: 1.6;
    }
    .email-body h2 {
      color: #AEBAC1;
    }
    .button {
      background-color: #00A884;
      color: #D9DEE0;
      padding: 12px 25px;
      border-radius: 5px;
      text-decoration: none;
      font-weight: bold;
    }
    .button:hover {
      background-color: #015C4B;
    }
    .email-footer {
      background-color: #272b2b;
      text-align: center;
      font-size: 12px;
      padding: 15px;
      color: #888;
    }
  </style>
</head>
<body>
  <div class="email-container">
    <div class="email-header">
      <img src="data:image/png;base64,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"
        alt="WhatsApp Clone Logo" class="logo">
    </div>
    <div class="email-body">
      <#nested>
    </div>
    <div class="email-footer">
      <p>© 2025 WhatsApp Clone App. All rights reserved.</p>
    </div>
  </div>
</body>
</html>
</#macro>
