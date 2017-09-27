/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nahon.comm.un.other;

/**
 *
 * @author Administrator
 */
public interface ProcessResult<T> {
    public int GetProcessPecent();
    public boolean IsFinished();
    public T GetResult() throws Exception;
}
