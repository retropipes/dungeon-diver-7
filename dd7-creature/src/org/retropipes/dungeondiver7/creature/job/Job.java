/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.job;

import org.retropipes.dungeondiver7.creature.job.description.JobDescriptionManager;

public class Job {
    static String jobIDtoName(final int jobID) {
	return JobConstants.JOB_NAMES[jobID];
    }

    private final int jobID;
    private final String desc;

    public Job(final int cid) {
	this.desc = JobDescriptionManager.getJobDescription(cid);
	this.jobID = cid;
    }

    public final int getJobID() {
	return this.jobID;
    }

    public String getDescription() {
	return this.desc;
    }
}
