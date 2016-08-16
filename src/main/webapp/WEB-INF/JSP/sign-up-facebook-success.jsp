<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div class="container">
	<div class="row">
		<div class="col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1 col-xs-12">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="panel-title">
						<i class="fa fa-thumbs-o-up" aria-hidden="true"></i>
						Sign up success
					</div>
				</div>
				<div class="panel-body">
					<h4>Congratulations!</h4>
					<p>You've been successfully signed up via Facebook.</p>
					<p>Some information from your facebook account was used to create your resume.</p>
					<p>To access to your profile w/o facebook please change password.</p>
					<p>Now your account will be available here: <a href="${appHost}/${profile.uid}">${appHost}/${profile.uid}</a></p>
					<p>Your UID: <strong>${profile.uid}</strong>. Use this UID to sign in.</p>
					<p>You may change your UID later.</p>
					<p>In order to avoid storing not relevant information, old records are deleted:</p>
					<ul>
						<li>Practical experience records after: ${practicYearsAgo} years</li>
						<li>Education records after: ${courseYearsAgo} years</li>
						<li>Courses records after: ${educationYearsAgo} years</li>
						<li>Full profiles after not being active: ${profileLastVisit} years</li>
					</ul>
					<a href="/my-profile" class="btn btn-primary">Continue</a>
				</div>
			</div>
		</div>
	</div>
</div>