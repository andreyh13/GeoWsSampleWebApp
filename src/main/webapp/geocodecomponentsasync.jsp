<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.sellbytel.GeoWsSampleWebApp.*" %>    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Geocode components filtering without address and asynchronously</title>
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</head>
<body>
	<header>
		<h1>Geocode components filtering without address and asynchronously</h1>
	</header>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
<%  GeoWsSamples s = new GeoWsSamples();
	String source = GeoWsSamples.showMethod(s, "testGeocodeComponentFilteringAsync");
%>	
	<p>In this example we try to geocode without address applying the components filtering and asynchronous approach</p>
	
	<p>
		Source code:
	</p>
	
	<code>
		<%= source %>
		<br/>
		<pre class="prettyprint">
		this.jspout = out;
		GeoApiContext context = new GeoApiContext().setEnterpriseCredentials(CLIENT_ID,
                CRYPTO_KEY).setQueryRateLimit(QPS);
		GeocodingApiRequest req = GeocodingApi.newRequest(context)
				.components(GeocodingApi.ComponentFilter.route("Annegatan"),
						GeocodingApi.ComponentFilter.administrativeArea("Helsinki"),
						GeocodingApi.ComponentFilter.country("Finland"));
		
		// Asynchronous
		req.setCallback(new PendingResult.Callback&lt;GeocodingResult[]&gt;() {
		  @Override
		  public void onResult(GeocodingResult[] result) {
			 try {
				jspout.println(printGeocodingResults(result));
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }

		  @Override
		  public void onFailure(Throwable e) {
			  System.out.println(e.getMessage());
		  }
		});
		</pre>
	</code>
	
	<p>
		Results:
	</p>
	<p>
		<% s.testGeocodeComponentFilteringAsync(out); %>
	</p>
	<a href="index.jsp" title="Go back">&lt; Go back</a>
</body>
</html>