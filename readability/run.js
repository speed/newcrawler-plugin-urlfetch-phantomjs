
function runReadability(url, userAgent, pageContent) {
	  var location = document.location;
	  var uri = {
	    spec: location.href,
	    host: location.host,
	    prePath: location.protocol + "//" + location.host, // TODO This is incomplete, needs username/password and port
	    scheme: location.protocol.substr(0, location.protocol.indexOf(":")),
	    pathBase: location.protocol + "//" + location.host + location.pathname.substr(0, location.pathname.lastIndexOf("/") + 1)
	  };
	  try {
	    var readabilityObj = new Readability(uri, document);
	    var isProbablyReaderable = readabilityObj.isProbablyReaderable();
	    var result = readabilityObj.parse();
	    if (result) {
	      result.userAgent = userAgent;
	      result.isProbablyReaderable = isProbablyReaderable;
	    } else {
	      result = {
	        error: {
	          message: "Empty result from Readability.js.",
	          sourceHTML: pageContent || "Empty page content."
	        }
	      };
	    }
	    return result;
	  } catch (err) {
	    return {
	      error: {
	        message: err.message,
	        line: err.line,
	        stack: err.stack,
	        sourceHTML: pageContent || "Empty page content."
	      }
	    };
	  }
	};

