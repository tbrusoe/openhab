package org.openhab.io.innovationhub.services.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A File service that will handle CRUD actions with the
 * .items, .sitemap , and .rule files associated with openHAB
 * @author Innohub
 */
@Path(FileService.PATH_FILES)
public class FileService {
	/* The URI path will be rest/files/ */
    public static final String PATH_FILES = "files";
	
	private static final Logger logger = LoggerFactory.getLogger(FileService.class); 
	protected static final String ITEMS_LOCATION = "./configurations/items/";
	protected static final String ITEMS_EXT = ".items";
	protected static final String RULES_LOCATION = "./configurations/rules/";
	protected static final String RULES_EXT = ".rules";
	protected static final String SITEMAPS_LOCATION = "./configurations/sitemaps/";
	protected static final String SITEMAPS_EXT = ".sitemap";
	protected static final String INNOHUB_ITEMS = "./configurations/items/innohub.items";
    
    @GET
    @Path("/items")
    @Produces({MediaType.APPLICATION_JSON})
    public String getItemFiles(){
		if (logger.isDebugEnabled()) logger.debug("Received HTTP GET request for item files");
		
		File dir = new File(FileService.ITEMS_LOCATION);
		HashMap<String,String> jsonObj = new HashMap<String,String>();
		
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(FileService.ITEMS_EXT)) {
				try {
					jsonObj.put(f.getName(), readFile(f.getAbsolutePath()));
				} catch (IOException e) {
					if (logger.isDebugEnabled()) logger.error("Failure to read file '{}'",f.getName());
				}
			}
		}
		String str = null;
		try {
			str = new ObjectMapper().writeValueAsString(jsonObj);
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to read convert map to string in getItemFiles");
		}
		return str;
    }
    
    @GET
    @Path("/items/{itemfile: [a-zA-Z_0-9]*}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response getItemFile(@PathParam("itemfile") String filename){
		if (logger.isDebugEnabled()) logger.debug("Received HTTP GET request for '{}' item file",filename);
		String path = FileService.ITEMS_LOCATION + filename + FileService.ITEMS_EXT;
		try {
			String content = readFile(path);
			return Response.ok(content).build();
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to read file '{}'",filename);
		}
		return Response.serverError().build();
    }
    
    @POST
    @Path("/items/innohub")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response appendItemFile(String value){
		if (logger.isDebugEnabled()) logger.debug("Received HTTP POST request for innohub item file");
		try {
			PrintWriter out = new PrintWriter(new FileWriter(INNOHUB_ITEMS, true));
			out.print(value);
			out.close();
			return Response.ok("Successfully wrote to innohub item file").build();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) logger.error("Failure to write to innohub items file");
		}
		return Response.serverError().build();
    }
    
    @POST
    @Path("/items/innohub/edit")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response editItemFile(String value){
		if (logger.isDebugEnabled()) logger.debug("Received HTTP POST request for innohub item file");
		try {
			PrintWriter out = new PrintWriter(INNOHUB_ITEMS);
			out.println(value);
			out.close();
			return Response.ok("Successfully wrote to innohub item file").build();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) logger.error("Failure to write to innohub items file");
		}
		return Response.serverError().build();
    }
    
    @DELETE
    @Path("/items/{itemfile: [a-zA-Z_0-9]*}")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response removeItemFile(@PathParam("itemfile") String filename){
		if (logger.isDebugEnabled()) logger.debug("Received HTTP DELETE request for '{}' item file",filename);
		File p = new File(FileService.ITEMS_LOCATION + filename + FileService.ITEMS_EXT);
		if(p.delete()){
			return Response.ok("Successfully deleted the file").build();
		}
		return Response.serverError().build();
    }
    
    @GET
    @Path("/sitemaps")
    @Produces({MediaType.APPLICATION_JSON})
    public String getSitemapFiles(){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP GET request for sitemaps files");
		
		File dir = new File(FileService.SITEMAPS_LOCATION);
		HashMap<String,String> jsonObj = new HashMap<String,String>();
		
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(FileService.SITEMAPS_EXT)) {
				try {
					jsonObj.put(f.getName(), readFile(f.getAbsolutePath()));
				} catch (IOException e) {
					if (logger.isDebugEnabled()) logger.error("Failure to read file '{}'",f.getName());
				}
			}
		}
		String str = null;
		try {
			str = new ObjectMapper().writeValueAsString(jsonObj);
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to read convert map to string in getSitemapFiles");
		}
		return str;
    }
    
    @GET
    @Path("/sitemaps/{sitemapfile: [a-zA-Z_0-9]*}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response getSitemapFile(@PathParam("sitemapfile") String filename){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP GET request for '{}' sitemap file",filename);
		String path = FileService.SITEMAPS_LOCATION + filename + FileService.SITEMAPS_EXT;
		try {
			String content = readFile(path);
			return Response.ok(content).build();
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to read file '{}'",filename);
		}
		return Response.serverError().build();
    }
    
    @POST
    @Path("/sitemaps/{sitemapfile: [a-zA-Z_0-9]*}")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response addSitemapFile(@PathParam("sitemapfile") String filename, String value){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP POST request for '{}' item file",filename);
		String path = FileService.SITEMAPS_LOCATION + filename + FileService.SITEMAPS_EXT;
		try {
			PrintWriter out = new PrintWriter(path);
			out.println(value);
			out.close();
			return Response.ok("Successfully created new sitemaps file").build();
		} catch (FileNotFoundException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to create file '{}'",filename);
		}
		return Response.serverError().build();
    }
    
    @DELETE
    @Path("/sitemaps/{sitemapfile: [a-zA-Z_0-9]*}")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response removeSitemapFile(@PathParam("sitemapfile") String filename){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP DELETE request for '{}' sitemap file",filename);
		File p = new File(FileService.SITEMAPS_LOCATION + filename + FileService.SITEMAPS_EXT);
		if(p.delete()){
			return Response.ok("Successfully deleted the file").build();
		}
		return Response.serverError().build();
    }
    
    @GET
    @Path("/rules")
    @Produces({MediaType.APPLICATION_JSON})
    public String getRuleFiles(){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP GET request for rule files");
		
		File dir = new File(FileService.RULES_LOCATION);
		HashMap<String,String> jsonObj = new HashMap<String,String>();
		
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(FileService.RULES_EXT)) {
				try {
					jsonObj.put(f.getName(), readFile(f.getAbsolutePath()));
				} catch (IOException e) {
					if (logger.isDebugEnabled()) logger.error("Failure to read file '{}'",f.getName());
				}
			}
		}
		String str = null;
		try {
			str = new ObjectMapper().writeValueAsString(jsonObj);
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to read convert map to string in getSitemapFiles");
		}
		return str;
    }
    
    @GET
    @Path("/rules/{rulefile: [a-zA-Z_0-9]*}")
    @Produces({MediaType.TEXT_PLAIN})
    public Response getRuleFile(@PathParam("rulefile") String filename){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP GET request for '{}' sitemap file",filename);
		String path = FileService.RULES_LOCATION + filename + FileService.RULES_EXT;
		try {
			String content = readFile(path);
			return Response.ok(content).build();
		} catch (IOException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to read file '{}'",filename);
		}
		return Response.serverError().build();
    }
    
    @POST
    @Path("/rules/{rulefile: [a-zA-Z_0-9]*}")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response addRuleFile(@PathParam("rulefile") String filename, String value){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP POST request for '{}' rule file",filename);
		String path = FileService.RULES_LOCATION + filename + FileService.RULES_EXT;
		try {
			PrintWriter out = new PrintWriter(path);
			out.println(value);
			out.close();
			return Response.ok("Successfully created new sitemaps file").build();
		} catch (FileNotFoundException e) {
			if (logger.isDebugEnabled()) logger.error("Failure to create file '{}'",filename);
		}
		return Response.serverError().build();
    }
    
    @DELETE
    @Path("/rules/{rulefile: [a-zA-Z_0-9]*}")
    @Consumes({MediaType.TEXT_PLAIN})
    public Response removeRuleFile(@PathParam("rulefile") String filename){
    	if (logger.isDebugEnabled()) logger.debug("Received HTTP DELETE request for '{}' rule file",filename);
		File p = new File(FileService.RULES_LOCATION + filename + FileService.RULES_EXT);
		if(p.delete()){
			return Response.ok("Successfully deleted the file").build();
		}
		return Response.serverError().build();
    }
    
    
    /* Utility function for reading a file */
    private String readFile(String path) throws IOException 
    {
		 byte[] encoded = Files.readAllBytes(Paths.get(path));
		 return new String(encoded, Charset.defaultCharset());
    }
}