package com.hust.baseweb.applications.education.core.solver.cblssolver;

import localsearch.model.ConstraintSystem;
import localsearch.model.VarIntLS;

public class BestSolution {

	private int[] X;
	private int violation;

	public BestSolution(VarIntLS[] x, ConstraintSystem s) {
		this.violation = s.violations();
		int n = x.length;
		X = new int[n];
		for (int i = 0; i < n; i++) {
			X[i] = x[i].getValue();
		}
	}

	public int getViolation() {
		return violation;
	}

	public int getX(int index) {
		return X[index];
	}

	public void update(VarIntLS[] x, ConstraintSystem s) {
		if (s.violations() < this.violation) {
			violation = s.violations();

			int n = x.length;
			for (int i = 0; i < n; i++) {
				X[i] = x[i].getValue();
			}
		}
	}
}