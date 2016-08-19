<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2 col-xs-12">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="panel-title">
						<i class="fa fa-unlock-alt" aria-hidden="true"></i>
						Profile settings
					</div>
				</div>
				<div class="panel-body">
					<input id="csrfToken" type="hidden" value="${_csrf.token}"/>
					<div class="panel-body">
						<div data-url="/edit/email" id="emailForm">
							<div class="form-group">
								<div class="alert alert-danger hidden" id="emailFormErrorDiv">
									<span id="emailFormErrorText"></span>
								</div>
								<label>New email</label>
								<c:set var="emailPopoverText" value="Email:
								provide your real email; 
								you will resive mail to confirm this adrress; 
								this adress will be used to send notifications; 
								this adress will be shown at your page." />
								<input name="email" type="text" class="form-control" placeholder="New email" value="${emailForm.email}" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${emailPopoverText}" />
							</div>
							<button type="button" class="btn btn-info pull-left submit-form-btn" data-form="emailForm">Change email</button>
						</div>
					</div>
					<hr />
					<div class="panel-body">
						<div data-url="/edit/password" id="passwordForm">
							<div id="passwordGroup" class="form-group">
								<div class="alert alert-danger hidden" id="passwordFormErrorDiv">
									<span id="passwordFormErrorText"></span>
								</div>
								<label class="control-label">New password</label>
								<c:set var="passwordPopoverText" value="Provide strong password:
								use english language only; 
								password should contain at least one digit, symbol, chars in lower case, upper case;  
								password should be longer than 7 symbols." />
								<input name="password" type="password" id="passwordInput" class="form-control" placeholder="New password" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${passwordPopoverText}" />
								<span class="help-block">
									<span id="passwordResultIcon" class="glyphicon hidden" aria-hidden="true"></span>
									<span id="passwordResultText" class="hidden">Strong password</span>
								</span>
							</div>
							<div class="form-group">
								<label>Confirm password</label>
								<input name="confirm" type="password" class="form-control" placeholder="Confirm password" />
							</div>
							<button type="button" class="btn btn-info pull-left submit-form-btn" data-form="passwordForm">Change password</button>
						</div>
					</div>
					<hr />
					<div class="panel-body">
						<div data-url="/edit/uid" id="uidForm">
							<div class="form-group">
								<div class="alert alert-danger hidden" id="uidFormErrorDiv">
									<span id="uidFormErrorText"></span>
								</div>
								<label>New UID</label>
								<c:set var="uidPopoverText" value="Provide your new uid:
								use english language only; 
								don't use any symbols." />
								<input name="uid" type="text" class="form-control" placeholder="New UID" value="${uidForm.uid}" data-toggle="popover" data-placement="top" data-trigger="focus" data-content="${uidPopoverText}" />
							</div>
							<button type="button" class="btn btn-info pull-left submit-form-btn" data-form="uidForm">Change uid</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>