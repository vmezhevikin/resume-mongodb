<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2 col-xs-12">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="panel-title">
						<i class="fa fa-user-plus" aria-hidden="true"></i>
						New profile
					</div>
				</div>
				<div class="panel-body">
					<form:form action="/sign-up" method="post" commandName="form">
						<div class="form-group">
							<label>Email</label>
							<c:set var="emailPopoverText" value="Email:
								provide your real email; 
								this adress will be shown at your page; 
								this adress will be used to send notifications; 
								this adress will be used for confirmation of your registration." />
							<input name="email" type="text" value="${form.email}" class="form-control" placeholder="Email" data-toggle="popover" data-placement="top" data-trigger="focus" />
							<form:errors path="email" cssClass="alert alert-danger" role="alert" element="div" />
						</div>
						<div id="passwordGroup" class="form-group">
							<label class="control-label">Password</label>
							<c:set var="passwordPopoverText" value="Provide strong password:
								use english language only; 
								password should contain at least one digit, symbol, chars in lower case, upper case;  
								password should be longer than 7 symbols." />
							<input name="password" type="password" id="passwordInput" class="form-control" placeholder="Password" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${passwordPopoverText}" />
							<span class="help-block">
								<span id="passwordResultIcon" class="glyphicon hidden" aria-hidden="true"></span>
								<span id="passwordResultText" class="hidden">Strong password</span>
							</span>
							<form:errors path="password" cssClass="alert alert-danger" role="alert" element="div" />
						</div>
						<div class="form-group">
							<label>Confirm password</label>
							<input name="confirm" type="password" class="form-control" placeholder="Confirm password" />
							<form:errors path="confirm" cssClass="alert alert-danger" role="alert" element="div" />
							<form:errors path="" cssClass="alert alert-danger" element="div" />
						</div>
						<div class="form-group">
							<div id="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
						</div>
						<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit&hl=en" async defer></script>
						<input name="recaptchaResponse" type="hidden" id="recaptchaResponse" />
						<form:errors path="recaptchaResponse" cssClass="alert alert-danger" role="alert" element="div" />
						<div class="form-group">
							<button type="submit" class="btn btn-primary">Sign up</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>