<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Elevation by path</title>
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</head>
<body>
	<header>
		<h1>Elevation by path</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "sampleElevationByPath");
	String result = s.sampleElevationByPath();
%>	
	<p>In this example we search elevation for path between 39.7391536,-104.9847034 and 36.455556,-116.866667</p>
	
	<p>
		Source code:
	</p>
	
	<code>
		<%= source %>
		<br/>
		<pre class="prettyprint">
		String output = "";
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		try {
			ElevationResult[] res = ElevationApi.getByPath(context, 3,  
					new LatLng(36.578581,-118.291994), new LatLng(36.455556,-116.866667))
					.await();
			output += this.printElevationResults(res);
		} catch(ApiException e){
			output += this.printError(e);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}	
		return output;
		</pre>
	</code>
	
	<p>
		Results:
	</p>
	<p>
		<%= result %>
	</p>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
</body>
</html>