<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="panel panel-primary">
	<div class="panel-heading">
		<div class="panel-title">
			<i class="fa fa-certificate" aria-hidden="true"></i>
			Certificates
			<sec:authorize access="hasAuthority('USER')">
				<sec:authentication var="currProfilieUid" property="principal.username" />
				<c:if test="${currProfilieUid == profile.uid}">
					<a class="pull-right" href="/edit/certificate">
						<i class="fa fa-cog" aria-hidden="true"></i>
					</a>
				</c:if>
			</sec:authorize>
		</div>
	</div>
	<div class="panel-body">
		<div class="row">
			<c:forEach var="certificate" items="${profile.certificate}">
				<div class="col-xs-3 col-md-3">
					<a href="${certificate.img}" class="thumbnail text-center">
						<img src="${certificate.imgSmall}" alt="certificate">
						<span>${certificate.description}</span>
					</a>
				</div>
			</c:forEach>
		</div>
	</div>
</div>