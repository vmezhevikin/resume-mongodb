<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="panel panel-primary">
	<div class="panel-heading">
		<div class="panel-title">
			<i class="fa fa-heart" aria-hidden="true"></i>
			Hobby
			<sec:authorize access="hasAuthority('USER')">
				<sec:authentication var="currProfilieId" property="principal.username" />
				<c:if test="${currProfilieId == profile.id}">
					<a class="pull-right" href="/edit/hobby">
						<i class="fa fa-cog" aria-hidden="true"></i>
					</a>
				</c:if>
			</sec:authorize>${principal.id}
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered">
			<c:forEach var="hobby" items="${hobbies}">
				<c:if test="${profile.hasHobby(hobby.name)}">
					<tr>
						<td class="text-center resume-hobby-td-icon">${hobby.icon}</td>
						<td class="text-center resume-hobby-td-name">${hobby.name}</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</div>
</div>