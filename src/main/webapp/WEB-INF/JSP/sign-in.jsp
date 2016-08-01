<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="container">
	<div class="row">
		<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2 col-xs-12">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="panel-title">
						<i class="fa fa-sign-in" aria-hidden="true"></i>
						Sign in
					</div>
				</div>
				<div class="panel-body">
					<p>You can use your UID or Email or Phone number as login.</p>
					<form:form action="/sign-in-handler" method="post">
						<c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null}">
							<div class="alert alert-danger" role="alert">
								<button type="button" class="close" data-dismiss="alert" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }
								<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
							</div>
						</c:if>
						<div class="form-group">
							<label>UID or Email or Phone number</label>
							<input name="uid" type="text" class="form-control" placeholder="UID or Email or Phone number" />
						</div>
						<div class="form-group">
							<label for="password">Password</label>
							<input name="password" type="password" class="form-control" placeholder="Password" />
						</div>
						<div class="checkbox">
							<label>
								<input name="remember-me" type="checkbox" />
								Remember me
							</label>
						</div>
						<div class="form-group sign-in-restore-block">
							<button type="submit" class="btn btn-primary pull-left">Sign in</button>
							<a href="/restore" class="pull-right">Restore access</a>
						</div>
						<hr />
						<div class="form-group fbLogin-block">
							<a href="/fbLogin" class="btn btn-success pull-center">Sign in via Facebook</a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>