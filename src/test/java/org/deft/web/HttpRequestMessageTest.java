package org.deft.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.deft.web.protocol.HttpRequestMessage;
import org.deft.web.util.ArrayUtil;
import org.junit.Test;


public class HttpRequestMessageTest {
	
	// curl http://127.0.0.1:8080/
	// |GET / HTTP/1.1|
	// |User-Agent: curl/7.19.5 (i386-apple-darwin10.0.0) libcurl/7.19.5 zlib/1.2.3|
	// |Host: 127.0.0.1:8080|
	// |Accept: */*|
	// ||
	// |                                                                                                                                                                                                                                                                                                                                                                                              |
	private static final byte[] raw1 = new byte[]{
		71,69,84,32,47,32,72,84,84,80,47,49,46,49,13,10,85,115,101,114,45,65,103,101,110,116,58,32,99,117,114,108,47,55,
		46,49,57,46,53,32,40,105,51,56,54,45,97,112,112,108,101,45,100,97,114,119,105,110,49,48,46,48,46,48,41,32,108,
		105,98,99,117,114,108,47,55,46,49,57,46,53,32,122,108,105,98,47,49,46,50,46,51,13,10,72,111,115,116,58,32,49,50,
		55,46,48,46,48,46,49,58,56,48,56,48,13,10,65,99,99,101,112,116,58,32,42,47,42,13,10,13,10};

	// firefox http://127.0.0.1:8080/
	// Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; sv-SE; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2
	// |GET / HTTP/1.1|
	// |Host: 127.0.0.1:8080|
    // |User-Agent: Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; sv-SE; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2|
	// |Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8|
	// |Accept-Language: sv-se,sv;q=0.8,en-us;q=0.5,en;q=0.3|
 	// |Accept-Encoding: gzip,deflate|
	// |Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7|
	// |Keep-Alive: 115|
	// |Connection: keep-alive|
	// ||
	// |                                                                                                                    |
	private static final byte[] raw2 = new byte[]{71,69,84,32,47,32,72,84,84,80,47,49,46,49,13,10,72,111,115,116,58,32,49,50,
		55,46,48,46,48,46,49,58,56,48,56,48,13,10,85,115,101,114,45,65,103,101,110,116,58,32,77,111,122,105,108,108,97,
		47,53,46,48,32,40,77,97,99,105,110,116,111,115,104,59,32,85,59,32,73,110,116,101,108,32,77,97,99,32,79,83,32,88,
		32,49,48,46,54,59,32,115,118,45,83,69,59,32,114,118,58,49,46,57,46,50,46,50,41,32,71,101,99,107,111,47,50,48,49,
		48,48,51,49,54,32,70,105,114,101,102,111,120,47,51,46,54,46,50,13,10,65,99,99,101,112,116,58,32,116,101,120,116,
		47,104,116,109,108,44,97,112,112,108,105,99,97,116,105,111,110,47,120,104,116,109,108,43,120,109,108,44,97,112,
		112,108,105,99,97,116,105,111,110,47,120,109,108,59,113,61,48,46,57,44,42,47,42,59,113,61,48,46,56,13,10,65,99,
		99,101,112,116,45,76,97,110,103,117,97,103,101,58,32,115,118,45,115,101,44,115,118,59,113,61,48,46,56,44,101,
		110,45,117,115,59,113,61,48,46,53,44,101,110,59,113,61,48,46,51,13,10,65,99,99,101,112,116,45,69,110,99,111,100,
		105,110,103,58,32,103,122,105,112,44,100,101,102,108,97,116,101,13,10,65,99,99,101,112,116,45,67,104,97,114,115,
		101,116,58,32,73,83,79,45,56,56,53,57,45,49,44,117,116,102,45,56,59,113,61,48,46,55,44,42,59,113,61,48,46,55,13,
		10,75,101,101,112,45,65,108,105,118,101,58,32,49,49,53,13,10,67,111,110,110,101,99,116,105,111,110,58,32,107,
		101,101,112,45,97,108,105,118,101,13,10,13,10};

	private static final ByteBuffer b1 = ByteBuffer.wrap(raw1); 
	private static final ByteBuffer b2 = ByteBuffer.wrap(raw2);
	
	@Test 
	public void testDeserializeHttpGetRequest() {
		HttpRequestMessage request1 = HttpRequestMessage.of(b1);
		HttpRequestMessage request2 = HttpRequestMessage.of(b2);
		
		assertEquals("GET / HTTP/1.1", request1.getRequestLine());
		assertEquals("GET / HTTP/1.1", request2.getRequestLine());
		
		assertEquals(3, request1.getHeaders().size());
		assertEquals(8, request2.getHeaders().size());
		
		List<String> expectedHeaderNamesInRequest1 = Arrays.asList(new String[]{"User-Agent", "Host", "Accept"});
		for (String expectedHeaderName : expectedHeaderNamesInRequest1) {
			assertTrue(request1.getHeaders().containsKey(expectedHeaderName));
		}
		
		List<String> expectedHeaderNamesInRequest2 = Arrays.asList(new String[]{"Host", "User-Agent", "Accept", 
				"Accept-Language", "Accept-Encoding", "Accept-Charset", "Keep-Alive", "Connection"});
		for (String expectedHeaderName : expectedHeaderNamesInRequest2) {
			assertTrue(request2.getHeaders().containsKey(expectedHeaderName));
		}
		
		// TODO RS 100920 verify that the headers exist
	}
	
	public void testRemoveTrailingEmptyStrings() {
		String fields1[] = new String[]{"a", "b", "c", "", ""};
		String fields2[] = new String[]{"a", "b", "c"};

		assertEquals(3, ArrayUtil.removeTrailingEmptyStrings(fields1).length);
		assertEquals(3, ArrayUtil.removeTrailingEmptyStrings(fields2).length);
	}
}
