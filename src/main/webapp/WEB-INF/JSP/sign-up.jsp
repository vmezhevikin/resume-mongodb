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
					<p class="text-danger">
						<strong>Warning!</strong>
						<br />
						<strong>Your name can not be changed after registration. Provide your real name.</strong>
					</p>
					<form:form action="/sign-up" method="post" commandName="form">
						<div class="form-group">
							<label>First name</label>
							<c:set var="firstNamePopoverText" value="Firstname:
								provide your real name (you won't be able to change it,
								it will be used to generate your uid); 
								use only english language only; don't use any symbols." />
							<input name="firstName" type="text" class="form-control" placeholder="Example: Richard" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${firstNamePopoverText}" />
							<form:errors path="firstName" cssClass="alert alert-danger" role="alert" element="div" />
						</div>
						<div class="form-group">
							<label>Last name</label>
							<c:set var="lastNamePopoverText" value="Lastname:
								provide your real name (you won't be able to change it,
								it will be used to generate your uid); 
								use only english language only; don't use any symbols." />
							<input name="lastName" type="text" class="form-control" placeholder="Example: Hendricks" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${lastNamePopoverText}" />
							<form:errors path="lastName" cssClass="alert alert-danger" role="alert" element="div" />
						</div>
						<div class="form-group">
							<label>Password</label>
							<c:set var="passwordPopoverText" value="Provide strong password:
								use english language only; 
								password nust contain at least one digit, symbol, chars in lower case, upper case;  
								password must be longer than 7 symbols." />
							<input name="password" type="password" class="form-control" placeholder="Password" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${passwordPopoverText}" />
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