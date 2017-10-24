package controllers;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import controllers.MyBusinessController;

public class ConcurrentProcesses implements Runnable {

	@Override
	public void run() {
		ClassExecutingTask executingTask = new ClassExecutingTask();
		executingTask.start();
	}

	public static void wasModifiedMyBusiness() {
		MyBusinessController.setWasModified(true);
		System.out.println("My Business Modified = true");

	}

	public static void wasModifiedMyFinance() {
		MyFinanceController.setWasModified(true);
		System.out.println("My Finance Modified = true");

	}

	public class ClassExecutingTask {

		long delay = 5 * 1000; // delay in milliseconds
		LoopTask task = new LoopTask();
		Timer timer = new Timer("TaskName");

		public void start() {
			MyBusinessController.setWasModified(false);
			MyFinanceController.setWasModified(false);
			timer.cancel();
			timer = new Timer("TaskName");
			Date executionDate = new Date(); // no params = now
			timer.scheduleAtFixedRate(task, executionDate, delay);
		}

		private class LoopTask extends TimerTask {
			public void run() {

				if (MyBusinessController.isWasModified()) {
					MyBusinessController.updateModifiedRow();
					MyBusinessController.setWasModified(false);
				} else if (MyFinanceController.isWasModified()) {
					MyFinanceController.updateModifiedRow();
					MyFinanceController.setWasModified(false);
				}
			}

		}

	}

	public static void stopProcess() {
		System.exit(0);
	}

}
