<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<%@ attribute name="index" required="true" type="java.lang.Object"%>
<%@ attribute name="course" required="false" type="net.devstudy.resume.domain.Course"%>
<%@ attribute name="minYear" required="true" type="java.lang.Object"%>
<%@ attribute name="maxYear" required="true" type="java.lang.Object"%>
<tr id="item-${index}">
	<td>
		<div class="row">
			<div class="panel panel-default">
				<table class="table">
					<tr>
						<td class="course-td-desc">
							<label>Description</label>
							<input name="items[${index}].description" type="text" class="form-control" placeholder="Description" value="${course.description}" />
							<form:errors path="items[${index}].description" cssClass="alert alert-danger" role="alert" element="div"/>
						</td>
						<td class="course-td-school">
							<label>School</label>
							<input name="items[${index}].school" type="text" class="form-control" placeholder="School" value="${course.school}" />
							<form:errors path="items[${index}].school" cssClass="alert alert-danger" role="alert" element="p" />
						</td>
						<td class="course-td-month">
							<label>Completion month</label>
							<select name="items[${index}].completionMonth" class="form-control">
								<option value="${null}">Not finished</option>
								<c:forEach var="month" begin="1" end="12">
									<option value="${month-1}" ${(month - 1) == course.completionMonth ? ' selected="selected"' : ''}>${monthName[month-1]}</option>
								</c:forEach>
							</select>
						</td>
						<td class="course-td-year">
							<label>Completion year</label>
							<select name="items[${index}].completionYear" class="form-control">
								<option value="${null}">Not finished</option>
								<c:forEach var="year" begin="${minYear}" end="${maxYear}">
									<option ${year == course.completionYear ? ' selected="selected"' : ''}>${year}</option>
								</c:forEach>
							</select>
						</td>
						<td class="text-muted td-close-btn">
							<button type="button" class="close remove-item-btn" aria-label="Close" id="closeBtn-${index}" data-item="${index}">
								<span aria-hidden="true">&times;</span>
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</td>
</tr>