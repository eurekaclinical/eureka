package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.ETL;

public class ProtempaDevice extends Thread {

    private Long jobId;
    private ETL runner = new ETL();
    //
    //	IMPORTANT:
    //	the synchObj is locked by the internal thread in ProtempaDeviceManager
    //	that calls load()
    //	and is locked by the runnable inside this ProtempaDevice instance.
    //
    private final Object synchObj = new Object();
    private volatile boolean busy = false;
    private volatile boolean failure = false;
    private final JobDao jobDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtempaDevice.class);

    public ProtempaDevice(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    void load(Long inJobId) {

        synchronized (synchObj) {

            LOGGER.debug("ProtempaDevice loaded with Job {}", inJobId);
            busy = true;
            this.jobId = inJobId;
            synchObj.notifyAll();	//	there should only ever be one thread wait()ing, but still...
        }
    }

    @Override
    public void run() {

        synchronized (synchObj) {
            try {
                while (true) {
                    Job myJob = null;
                    try {

                        LOGGER.debug("{} wait.", Thread.currentThread().getName());
                        synchObj.wait();
                        myJob = this.jobDao.retrieve(this.jobId);
                        LOGGER.debug("{} just got a job, id={}", Thread.currentThread().getName(), myJob.toString());
                        myJob.setNewState("PROCESSING", null, null);
                        LOGGER.debug("About to save job: {}", myJob.toString());
                        this.jobDao.update(myJob);

                        Long configId = myJob.getConfigurationId();
                        runner.run("config" + configId);

                        myJob.setNewState("DONE", null, null);
                        this.jobDao.update(myJob);
                    } catch (InterruptedException ie) {

                        LOGGER.debug("{} interrupted and finished.", Thread.currentThread().getName());
                        this.failure = true;
                        if (myJob != null) {

                            myJob.setNewState("INTERRUPTED", null, null);
                            this.jobDao.update(myJob);
                        }
                        return;
                    } catch (Exception e) {

                        e.printStackTrace();
                        LOGGER.debug("{} unknown exception and finished. {}", Thread.currentThread().getName(), e.getMessage());
                        this.failure = true;
                        if (myJob != null) {

                            myJob.setNewState("EXCEPTION", null, null);
                            this.jobDao.update(myJob);
                        }
                        return;
                    } finally {

                        busy = false;
                    }
                }
            } finally {
                this.runner.close();
            }
        }
    }

    boolean isBusy() {

        return this.busy;
    }

    boolean hasFailed() {

        return this.failure;
    }

    Job getJob() {

        return this.jobDao.retrieve(this.jobId);
    }

    protected void tagJob(Thread thread, Throwable t) {

        Job myJob = this.jobDao.retrieve(this.jobId);
        if (myJob != null) {
            //	recordException
        }
    }
}
