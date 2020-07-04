public class EraseThread implements Runnable{
	private boolean stop;
	public EraseThread(String prompt){
		System.out.print(prompt);
	}

	public void run(){
		stop = true;
		while(stop){
			System.out.print("\010*");
			try{
				Thread.currentThread().sleep(1);
			} catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}

	public void stopMasking(){
		this.stop = false;
	}
}