package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

/**
 *
 * @author hrathod
 */
public class JobResourceTest extends AbstractEtlResourceTest {

	/**
	 * Test if all the jobs added by the Setup class are returned properly,
	 * using a null Filter.
	 */
	@Test
	public void testJobListWithFilter() {
		WebResource resource = this.resource();
		JobFilter jobFilter = new JobFilter(null, null, null, null, null);
		List<Job> jobs = resource.path("/api/job/status").queryParam("filter",
				jobFilter.toQueryParam()).accept(
				MediaType.APPLICATION_JSON).get(new GenericType<List<Job>>() {
		});
		Assert.assertEquals(1, jobs.size());
	}

	@Test
	public void testJobList () {
		WebResource resource = this.resource();
		List<Job> jobs = resource.path("/api/job/list").accept(
				MediaType.APPLICATION_JSON).get(new GenericType<List<Job>>() {
		});
		Assert.assertEquals(1, jobs.size());
	}

}
