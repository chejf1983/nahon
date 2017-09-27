/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.drv.update;

/**
 *
 * @author Administrator
 */
public interface IProcess {

    public void SetPecent(float pecent);

    public float GetPecent();
    
    public boolean IsFinished();
    
    public boolean IsSucess();
}
