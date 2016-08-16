<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="panel panel-primary">
	<div class="panel-heading">
		<div class="panel-title">
			<i class="fa fa-language" aria-hidden="true"></i>
			Foreign languages
			<sec:authorize access="hasAuthority('USER')">
				<sec:authentication var="currProfilieId" property="principal.username" />
				<c:if test="${currProfilieId == profile.id}">
					<a class="pull-right" href="/edit/language">
						<i class="fa fa-cog" aria-hidden="true"></i>
					</a>
				</c:if>
			</sec:authorize>
		</div>
	</div>
	<div class="panel-body">
		<c:forEach var="language" items="${profile.language}">
			<p>
				<strong>${language.name}: </strong>
				${language.level} (
				<em>${language.type}</em>
				)
			</p>
		</c:forEach>
	</div>
</div>