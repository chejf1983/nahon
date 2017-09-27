package nahon.comm.un.other;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import nahon.comm.event.EventCenter;

/**
 *
 * @author jiche
 */
public abstract class ProcessModel {

    public static final int CLOSE = 0x00;
    public static final int SUCCESS = 0x01;
    public static final int PROCESSING = 0x02;
    public static final int START = 0x03;
    public static final int ERROR = 0x04;

    public EventCenter<Integer> EventCenter = new EventCenter();
    private int state = ProcessModel.CLOSE;
    private Thread process;

    /**
     *
     * @param state
     * @param info
     */
    protected void ChangeState(int state, Object info) {
        this.state = state;
        this.EventCenter.CreateEvent(state, info);
    }

    public int GetCurrentState() {
        return this.state;
    }

    public void Start() {
        if (this.state == ProcessModel.CLOSE) {
            this.process = new Thread(new Runnable() {
                @Override
                public void run() {
                    Excute();
                }
            });
            this.process.start();
            this.ChangeState(ProcessModel.START, null);
        }
    }

    public void Stop() {
        if (this.state != ProcessModel.CLOSE) {
            this.process.stop();
            this.ChangeState(ProcessModel.CLOSE, null);
        }
    }

    protected abstract void Excute();
}
