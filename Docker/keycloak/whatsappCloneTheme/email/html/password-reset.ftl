<#import "email-layout.ftl" as layout>
<@layout.emailLayout>
  <h2>Reset Your Password</h2>

  <p>Hi ${user.firstName!user.username!""},</p>

  <p>We received a request to reset your password for your <strong>WhatsApp Clone</strong> account.</p>

  <p style="text-align:center; margin: 30px 0;">
    <a href="${link}" class="button">Reset Password</a>
  </p>

  <p>If you didn’t request a password reset, you can safely ignore this email.</p>

  <p>For security reasons, this link will expire in <strong>${linkExpiration!30}</strong> minutes.</p>

  <br/>
  <p>Thanks,<br>The WhatsApp Clone Team</p>
</@layout.emailLayout>
