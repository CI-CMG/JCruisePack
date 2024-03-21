package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import java.beans.PropertyChangeListener;

record PackingJob(PackJob packJob, Runnable executeBefore, Runnable executeAfter) {}
