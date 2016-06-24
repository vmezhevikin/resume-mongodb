<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<%@ attribute name="index" required="true" type="java.lang.Object"%>
<%@ attribute name="skill" required="false" type="net.devstudy.resume.domain.Skill"%>
<tr>
	<td width="20%">
		<select name="items[${index}].category" class="form-control">
			<c:forEach var="category" items="${skillCategories}">
				<option value="${category.name}" ${category.name == skill.category ? ' selected="selected"' : ''}>${category.name}</option>
			</c:forEach>
		</select>
	</td>
	<td width="77%">
		<textarea name="items[${index}].description" class="form-control" rows="3" style="resize: none;" required="required" placeholder="Description">${skill.description}</textarea>
		<form:errors path="items[${index}].description" cssClass="alert alert-danger" role="alert" element="div" />
	</td>
	<td class="text-muted" width="3%">
		<button type="button" class="close" aria-label="Close" onclick="deleteRow(this)">
			<span aria-hidden="true">&times;</span>
		</button>
	</td>
</tr>