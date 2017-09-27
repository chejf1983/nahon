/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table.watch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import nahon.comm.exl.AbstractExcelTable;
import nahon.comm.exl.DefaultExcelTable;
import nahon.comm.filter.Theleastsquaremethod;
import nahon.comm.tool.languange.LanguageHelper;

/**
 *
 * @author Administrator
 */
public class WatchBean {

    // <editor-fold defaultstate="collapsed" desc="数据刷新">
    //数据锁
    private final ReentrantLock nodeslock = new ReentrantLock();
    //数据精度
    private int precision = 2;

    //设置数据精度
    public void SetPrec(int num) {
        this.precision = num;
    }

    //刷新点值
    public void UpdateValue(double[] nm, double[] value) {
        this.nodeslock.lock();

        try {
            for (WatchNode node : GetNodes()) {
//                double predict = Newton.predict(nm, value, 10, node.GetNmValue());
                //最小二项式预测法
                double predict = Theleastsquaremethod.predict(nm, value, 20, node.GetNmValue());
                predict = new BigDecimal(predict).setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
                //刷新节点数据
                node.UpdateCurrentValue(predict);
                //记录历史数据
                if (this.IsHistoryEnable()) {
                    node.UpdateHistory(predict);
                }
            }
        } finally {
            this.nodeslock.unlock();
        }
    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="观察点">
    //观察节点组
    private ArrayList<WatchNode> nodes = new ArrayList();

    //获取观察节点
    public WatchNode[] GetNodes() {
        return this.nodes.toArray(new WatchNode[0]);
    }

    //添加观察节点
    public void AddWatchNode(double nm) {
        //在历史数据使能的情况下不允许增加新节点，保持所有点历史记录长度相同
        if (this.isHistoryEnable) {
            return;
        }

        //上锁
        nodeslock.lock();
        try {
            //检查节点是否已经存在
            for (WatchNode node : nodes) {
                if (node.GetNmValue() == nm) {
                    return;
                }
            }
            //添加新节点
            this.nodes.add(new WatchNode(nm));
        } finally {
            nodeslock.unlock();
        }
    }

    //删除节点
    public void RemoveIndex(int index) {
        nodeslock.lock();
        try {
            //检查序号是否合法
            if (index >= 0 && index < this.nodes.size()) {
                nodes.remove(index);
            }
        } finally {
            nodeslock.unlock();
        }
    }

    //清除所有节点
    public void Clear() {
        nodeslock.lock();
        try {
            nodes.clear();
        } finally {
            nodeslock.unlock();
        }
    }
    // </editor-fold>     

    // <editor-fold defaultstate="collapsed" desc="观察记录使能">
    private boolean isHistoryEnable = false;

    public boolean IsHistoryEnable() {
        return this.isHistoryEnable;
    }

    public void EnableHistory(boolean value) {
        this.isHistoryEnable = value;
        //如果是关闭历史记录，清除所有节点的历史记录
        if (!value) {
            nodeslock.lock();
            try {
                for (WatchNode node : nodes) {
                    node.ClearHistory();
                }
            } finally {
                nodeslock.unlock();
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="转EXCEL表格"> 
    //观察点数据名称
    private String valuename = "Original";

    public void SetValueName(String name) {
        this.valuename = name;
    }

    //获取观察点EXCEL表格
    public AbstractExcelTable WatchTable() {
        if (this.nodes.isEmpty()) {
            return null;
        }

        //创建EXCEL空表
        DefaultExcelTable table = DefaultExcelTable.createTable(
                LanguageHelper.getIntance().GetText("History"),
                new String[]{LanguageHelper.getIntance().GetText("WaveIndex"),
                    LanguageHelper.getIntance().GetText(this.valuename)});

        //添加行数据
        for (int i = 0; i < this.nodes.size(); i++) {
            table.AddRow(new Object[]{this.nodes.get(i).GetNmValue(), this.nodes.get(i).GetCurrentValue()});
        }

        return table;
    }

    //获取所有节点历史数据表格
    public AbstractExcelTable[] HistoryToExcelTable() {
        if (!this.IsHistoryEnable()) {
            return new AbstractExcelTable[0];
        }

        nodeslock.lock();
        try {
            DefaultExcelTable[] tables = new DefaultExcelTable[this.nodes.size()];

            for (int i = 0; i < tables.length; i++) {
                //创建EXCEL空表
                tables[i] = DefaultExcelTable.createTable(
                        LanguageHelper.getIntance().GetText("WaveIndex")
                        + "[" + this.nodes.get(i).GetNmValue() + "]",
                        new String[]{"", LanguageHelper.getIntance().GetText(this.valuename)});

                //添加行数据
                Double[] history = this.nodes.get(i).GetHistory();
                for (int j = 0; j < history.length; j++) {
                    tables[i].AddRow(new Object[]{j, history[j]});
                }
            }
            return tables;
        } finally {
            nodeslock.unlock();
        }
    }
    // </editor-fold>
}
