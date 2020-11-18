package com.hust.baseweb.applications.education.teacherclassassignment.service;

import localsearch.constraints.basic.NotEqual;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class CBLSSolver {
    private int n;
    private int m;
    private HashSet<Integer>[] D;
    private boolean[][] conflict;

    private LocalSearchManager mgr;
    private ConstraintSystem S;
    private VarIntLS[] x;
    public CBLSSolver(int n, int m, HashSet[] D, boolean[][] conflict){
        this.n = n; this.m = m;
        this.D = D; this.conflict = conflict;
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
    private void search(int maxIter, int maxTime){
        double t0 = System.currentTimeMillis();
        ArrayList<Move> cand = new ArrayList<Move>();
        Random R = new Random();
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
            System.out.println("Step " + it + ": " + S.violations());
            if(S.violations() == 0) break;
        }
    }
    public void solve(){
        stateModel();
        search(10000,10000);
    }
    public int[] getSolution(){
        int[] s = new int[x.length];
        for(int i = 0; i < n; i++) s[i] = x[i].getValue();
        return s;
    }
}
