package com.cookingwebsite.crud.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cookingwebsite.crud.email.SendEmails;

public class SendEmailsUtils {

	/**
	 * @param parameters HashMap<String, ?> - Mandatory fields: firstname (String) - username (String) - password (String) -
	 *                   email (List[String] or String) - isNewUser (Boolean)
	 */
	public static void sendUserCredentials(HashMap<String, ?> parameters) {

		StringBuilder body = new StringBuilder("");
		body.append("<p>Dear");
		if (parameters.containsKey("firstname") && (parameters.get("firstname") instanceof String)) {
			body.append(" ").append((String) parameters.get("firstname"));
		} else {
			body.append(" user");
		}
		body.append(",&nbsp;</p>");
		if (parameters.containsKey("isNewUser") && (parameters.get("isNewUser") instanceof Boolean)) {
			if ((Boolean) parameters.get("isNewUser")) {
				body.append("<p>Welcome to <strong>Cooking Web Site</strong>. We are providing you with your temporary credentials.</p>");
			} else {
				body.append("<p>Credentials have been successfully modified.</p>");
			}
		}
		if (parameters.containsKey("isNewUser") && (parameters.get("isNewUser") instanceof Boolean)) {
			if ((Boolean) parameters.get("isNewUser")) {
				body.append("<p></p>");
				body.append("<table class=\"demoTable\" style=\"height: 62px;\" width=\"613\">");
				body.append("<thead>");
				body.append("<tr>");
				body.append("<td style=\"width: 351.016px;\"><span style=\"color: #c82828;\">Username</span></td>");
				body.append("<td style=\"width: 352.984px;\"><span style=\"color: #c82828;\">Password</span></td>");
				body.append("</tr>");
				body.append("</thead>");
				body.append("<tbody>");
				body.append("<tr>");
				body.append("<td style=\"width: 351.016px;\">");
				if (parameters.containsKey("username") && (parameters.get("username") instanceof String)) {
					body.append((String) parameters.get("username"));
				}
				body.append("</td>");
				body.append("<td style=\"width: 352.984px;\">");
				if (parameters.containsKey("password") && (parameters.get("password") instanceof String)) {
					body.append((String) parameters.get("password"));
				}
				body.append("</td>");
				body.append("</tr>");
				body.append("</tbody>");
				body.append("</table>");
				body.append("<p></p>");
			}
		}
		body.append(
				"<p>To access the application you can click <a href=\"http://localhost:4200/cooking-website/login\" target=\"_blank\" rel=\"noopener\">here</a>.</p>");
		body.append("<p>Thanks a lot,</p>");
		body.append("<p>Best regards,</p>");
		body.append("<p></p>");
		body.append("<p><strong>Cooking Web Site Team</strong></p>");
		List<String> to = new ArrayList<String>();
		to.add("endika.fernandez@twtspain.com");
		if (parameters.containsKey("email")) {
			if (parameters.get("email") instanceof String) {
				to.add((String) parameters.get("email"));
			}
			if (parameters.get("email") instanceof List<?>) {
				List<?> toAux = (List<?>) parameters.get("email");
				for (Object email : toAux) {
					if (email instanceof String) {
						to.add((String) email);
					}
				}
			}
		}

		SendEmails.SendEmail(to, "Asunto", body.toString());
	}

}
