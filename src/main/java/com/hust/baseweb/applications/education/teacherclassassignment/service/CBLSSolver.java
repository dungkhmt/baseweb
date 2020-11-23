package com.hust.baseweb.applications.education.teacherclassassignment.service;

import localsearch.constraints.basic.NotEqual;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class CBLSSolver {
    private int n;// number of classes
    private int m;// number of teachers
    private HashSet<Integer>[] D;
    private boolean[][] conflict;
    private double[] hourClass;
    private LocalSearchManager mgr;
    private ConstraintSystem S;
    private VarIntLS[] x;
    private Random R = new Random();
    private int[] solution;
    public CBLSSolver(int n, int m, HashSet[] D, boolean[][] conflict, double[] hourClass){
        this.n = n; this.m = m;
        this.D = D; this.conflict = conflict;
        this.hourClass  = hourClass;
    }
    private void stateModel(){
        mgr = new LocalSearchManager();
        x = new VarIntLS[n];
        for(int i = 0; i < n; i++) x[i] = new VarIntLS(mgr,D[i]);
        S = new ConstraintSystem(mgr);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(conflict[i][j]){
                    S.post(new NotEqual(x[i],x[j]));
                }
            }
        }
        mgr.close();
    }
    class Move{
        int i,v;
        public Move(int i, int v){
            this.i = i; this.v = v;
        }
    }
    private HashSet<Integer> initSolution(){
        HashSet<Integer> cand = new HashSet();
        HashSet<Integer> assigned = new HashSet();
        HashSet<Integer> notAssigned = new HashSet<>();
        for(int i = 0; i < n; i++) cand.add(i);
        double[] load = new double[m];
        for(int t = 0; t < m; t++) load[t] = 0;
        while(cand.size() > 0){
            int minD = Integer.MAX_VALUE;
            int sel_i = -1;
            for(int i: cand){
                if(D[i].size() < minD){
                    minD = D[i].size(); sel_i = i;
                }
            }
            // select teacher t for class sel_i such that load[t] is minimal
            double minLoad = Integer.MAX_VALUE;
            int sel_t = -1;
            for(int t: D[sel_i]){
                boolean ok = true;
                for(int j: assigned)if(conflict[sel_i][j] && x[j].getValue() == t){
                    ok = false; break;
                }
                if(!ok) continue;
                if(load[t]  < minLoad){
                    minLoad = load[t]; sel_t = t;
                }
            }
            if(sel_t == -1){
                notAssigned.add(sel_i);
            }else {
                x[sel_i].setValuePropagate(sel_t);
                assigned.add(sel_i);
                load[sel_t] += hourClass[sel_i];
            }
            cand.remove(sel_i);
        }
        System.out.println("After INIT, not assigned = " + notAssigned.size());
        return notAssigned;
    }
    private HashSet<Integer> search(int maxIter, int maxTime){
        HashSet<Integer> notAssigned = initSolution();
        if(true) return notAssigned;

        double t0 = System.currentTimeMillis();
        ArrayList<Move> cand = new ArrayList<Move>();
        //Random R = new Random();
        for(int it = 0; it < maxIter; it++){
            if(System.currentTimeMillis() - t0 > maxTime){
                break;
            }
            cand.clear();
            int minD = Integer.MAX_VALUE;
            for(int i = 0; i < n; i++){
                for(int v: D[i]){
                    int d = S.getAssignDelta(x[i],v);
                    if(d < minD){
                        minD = d;
                        cand.clear();
                        cand.add(new Move(i,v));
                    }else if(d == minD){
                        cand.add(new Move(i,v));
                    }
                }
            }
            Move m = cand.get(R.nextInt(cand.size()));
            x[m.i].setValuePropagate(m.v);
            //System.out.println("Step " + it + ": " + S.violations());
            if(S.violations() == 0) break;
        }
        return notAssigned;
    }
    public void solve(){
        stateModel();
        HashSet<Integer> notAssign = search(10000,10000);
        solution= new int[x.length];
        for(int i = 0; i < n; i++) {
            if (notAssign.contains(i)) {
                solution[i] = x[i].getValue();
            }else{
                solution[i] = -1;
            }
        }
    }
    public int[] getSolution(){
        //int[] s = new int[x.length];
        //for(int i = 0; i < n; i++) s[i] = x[i].getValue();
        //return s;
        return solution;
    }
}
