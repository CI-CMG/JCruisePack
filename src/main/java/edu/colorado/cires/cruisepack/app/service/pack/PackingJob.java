package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

record PackingJob(PackJob packJob, String processId, Runnable executeBefore, Consumer<Boolean> executeAfter) {}
