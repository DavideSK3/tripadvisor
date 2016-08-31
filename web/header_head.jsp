<!-- Inclusione css di Bootstrap, Jquery e styles.css;
     Definizione del charset utilizzato e dell' icona personalizzata del portale --> 

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="<c:out value='${pageContext.servletContext.contextPath}'/>/styles.css">
<link href="http://code.jquery.com/ui/1.11.4/themes/redmond/jquery-ui.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="<c:out value='${pageContext.servletContext.contextPath}'/>/favicon.ico" type="image/ico" />
