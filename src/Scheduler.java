import java.util.LinkedList;
import java.util.Queue;

public class Scheduler extends Thread{
	boolean busy = false;
	Process runningProcess;
	Queue<Process> schedulerQueue = new LinkedList<Process>();

	public void run() {
		while (!schedulerQueue.isEmpty() && !OperatingSystem.readyQueue.isEmpty()) {
			try {
				this.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			runningProcess = schedulerQueue.peek();

			if (Process.getProcessState(runningProcess) == ProcessState.Ready) {
				if (runningProcess.run == false)
					runningProcess.start();
				else
					runningProcess.resume();
				busy = true;
			}

			else if (Process.getProcessState(runningProcess) == ProcessState.Terminated) {
				schedulerQueue.poll();
				if (schedulerQueue.isEmpty())
					busy = false;
			}
			else if (Process.getProcessState(runningProcess) == ProcessState.Waiting) {
				Process blockedProcess = schedulerQueue.poll();
				blockedProcess.suspend();
				schedulerQueue.add(blockedProcess);
			}
		}
	}

	public void load(Process process) {
		schedulerQueue.add(process);
		try {
			this.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (busy == false)
			this.start();
	}
}
