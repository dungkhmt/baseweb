package com.hust.baseweb.applications.education.core.solver.cblssolver;

import com.hust.baseweb.applications.education.core.solver.BCA_Problem;
import localsearch.constraints.basic.NotEqual;
import localsearch.constraints.multiknapsack.MultiKnapsack;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.standarddeviation.StandardDeviation;
import localsearch.model.*;
import localsearch.search.TabuSearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CBLSSolver {

	private LocalSearchManager mgr;
	private ConstraintSystem S;
	private VarIntLS[] X;
	private IFunction[] nbCredits;
	private IFloatFunction obj;

	private ArrayList<Move> move;
	private Random r;

	private Set<Integer> exception;
	private BCA_Problem problem;
	private BestSolution best;

	public CBLSSolver(BCA_Problem problem) {
		this.problem = problem;
		this.exception = new HashSet<Integer>();
		this.r = new Random(0);
	}

	public void buildPreProcessModel() {
		int N = problem.numClass;
		int M = problem.numTeacher;
		mgr = new LocalSearchManager();
		X = new VarIntLS[N];
		for (int i = 0; i < N; i++) {
			Set<Integer> domain = new HashSet<Integer>();
			for (int v : problem.possibleTeacherForClass[i])
				domain.add(v);
			if (domain.isEmpty()) {
				domain.add(M);
			}
			X[i] = new VarIntLS(mgr, domain);
		}

		S = new ConstraintSystem(mgr);
		for (int[] conflict : problem.conflictPairs) {
			S.post(new NotEqual(X[conflict[0]], X[conflict[1]]));
		}

		int maxCredit[] = new int[M + 1];
		for (int i = 0; i < M; i++) {
			maxCredit[i] = problem.maxCredit[i];
		}
		maxCredit[M] = 100;
		S.post(new MultiKnapsack(X, problem.creditOfClass, maxCredit));

		mgr.close();
	}

	public void executePreProcess(int tabulen, int maxTime, int maxIter, int maxStable) {
		int N = problem.numClass;
		int M = problem.numTeacher;
		maxTime *= 1000;
		int count = 0;
		int nic = 0;
		int[][] tabu = new int[N][M + 1];
		long startTime = System.currentTimeMillis();
		move = new ArrayList<Move>();
		for (int i = 0; i < N; i++) {
			if (problem.possibleTeacherForClass[i].size() == 1) {
				X[i].setValuePropagate(problem.possibleTeacherForClass[i].get(0));
			}
		}

		best = new BestSolution(X, S);
		VarIntLS[] x = S.getVariables();
		int bestV = S.violations();
		int init = bestV;
		System.out.println("Initial S: " + init);
		init /= 10;

		while (count++ < maxIter && (System.currentTimeMillis() - startTime < maxTime) && S.violations() > 0) {
			move.clear();
			int minDelta = 10000;
			for (int i = 0; i < N; i++) {
				if (problem.possibleTeacherForClass[i].size() == 1)
					continue;
				for (int j : x[i].getDomain()) {
					int delta = S.getAssignDelta(x[i], j);
					if (bestV > delta + S.violations() || tabu[i][j] <= count) {
						if (delta == minDelta) {
							move.add(new Move(i, j));
						} else if (delta < minDelta) {
							minDelta = delta;
							move.clear();
							move.add(new Move(i, j));
						}
					}
				}
			}
			int size = move.size();
			if (size > 0) {
				Move m = move.get(r.nextInt(size));
				int i = m.x, j = m.y;
				x[i].setValuePropagate(j);
				tabu[i][j] = count + tabulen;
			}

			if (best.getViolation() > S.violations()) {
				best.update(X, S);
				nic = 0;
			} else {
				nic++;
			}

			System.out.println("Preprocessing, step " + count + ": violation = " + best.getViolation()
					+ ", nic = " + nic);
			if (nic >= maxStable) {
				nic = 0;
				executePerturbation(tabu);
			}
		}

		int result[] = new int[N];
		for (int i = 0; i < N; i++) {
			result[i] = best.getX(i);
			X[i].setValuePropagate(best.getX(i));
		}
		for (int i = 0; i < N; i++) {
			if (S.violations(X[i]) > 0 || result[i] == M) {
				this.exception.add(i);
				result[i] = -1;
			}
		}
		System.out.println("Violations: " + S.violations());
		System.out.println("conflict num: " + this.exception.size());
		for (int i : this.exception) {
			System.out.println("Detect conflict: " + i);
		}

//		this.check(result);
	}

	private void executePerturbation(int[][] tabu) {
		int N = problem.numClass;
		int M = problem.numTeacher;

		for (int i = 0; i < N; i++) {
			X[i].setValuePropagate(best.getX(i));
		}

		for (int i = 0; i < N; i++) {
			ArrayList<Integer> domain = new ArrayList<Integer>();
			for (int v : X[i].getDomain()) {
				if (S.getAssignDelta(X[i], v) <= 0)
					domain.add(v);
			}
			X[i].setValuePropagate(domain.get(r.nextInt(domain.size())));
		}

		for (int i = 0; i < tabu.length; i++) {
			for (int j = 0; j < tabu[0].length; j++)
				tabu[i][j] = -1;
		}
	}

	public void buildOptimizationModel() {
		int N = problem.numClass;
		int M = problem.numTeacher;
		mgr = new LocalSearchManager();
		VarIntLS[] tmpX = X;
		X = new VarIntLS[N];
		for (int i = 0; i < N; i++) {
			if (this.exception.contains(i)) {
				X[i] = new VarIntLS(mgr, M, M);
			} else {
				X[i] = new VarIntLS(mgr, tmpX[i].getDomain());
			}
		}

		S = new ConstraintSystem(mgr);
		for (int[] conflict : problem.conflictPairs) {
			if (!this.exception.contains(conflict[0])) {
				S.post(new NotEqual(X[conflict[0]], X[conflict[1]]));
			}
		}

		int maxCredit[] = new int[M + 1];
		for (int i = 0; i < M; i++) {
			maxCredit[i] = problem.maxCredit[i];
		}
		maxCredit[M] = 100;
		S.post(new MultiKnapsack(X, problem.creditOfClass, maxCredit));

		nbCredits = new IFunction[M];
		for (int i = 0; i < M; i++) {
			nbCredits[i] = new ConditionalSum(X, problem.creditOfClass, i);
		}

		obj = new StandardDeviation(nbCredits);

		mgr.close();

		this.mergeModels();
	}

	public void mergeModels() {
		int N = problem.numClass;
		int M = problem.numTeacher;

		for (int i = 0; i < N; i++) {
			X[i].setValuePropagate(best.getX(i));
		}

		int bestV = S.violations();
		System.out.println("S: " + bestV);
		int count = 0;
		while (S.violations() > 0) {
			move.clear();
			int minDelta = 100000;
			int n = S.getVariables().length;
			VarIntLS[] x = S.getVariables();
			for (int i = 0; i < n; i++) {
				if (problem.possibleTeacherForClass[i].size() <= 1)
					continue;
				for (int j : x[i].getDomain()) {
					int delta = S.getAssignDelta(x[i], j);
					if (bestV > delta + S.violations()) {
						if (delta == minDelta) {
							move.add(new Move(i, j));
						} else if (delta < minDelta) {
							minDelta = delta;
							move.clear();
							move.add(new Move(i, j));
						}
					}
				}
			}
			int size = move.size();
			if (size > 0) {
				Move m = move.get(r.nextInt(size));
				int i = m.x, j = m.y;
				x[i].setValuePropagate(j);
			}
			System.out.println(
					"Merging two models, step " + count++ + ": S = " + S.violations() + ", delta = " + minDelta);
		}
	}

	private boolean check(int[] result) {
		int N = problem.numClass;
		int M = problem.numTeacher;
		if (result.length < N) {
			System.err.println("Error! result size = " + result.length + ", N = " + N);
			return false;
		}
		boolean flag = true;
		int nbCredit[] = new int[M];

		for (int i = 0; i < N; i++) {
			if (result[i] >= 0) {
				nbCredit[result[i]] += problem.creditOfClass[i];
				if (!problem.possibleTeacherForClass[i].contains(result[i])) {
					System.err.println(
							"Error::PossibleTeacherForClass: class " + i + " cannot assigned to teacher " + result[i]);
					flag = false;
				}
			}
		}

		for (int[] t : problem.conflictPairs) {
			if (result[t[0]] == result[t[1]] && result[t[0]] >= 0) {
				System.err.println(
						"Error::Conflict: class " + t[0] + " and class " + t[1] + " cannot assigned to one teacher");
				flag = false;
			}
		}

		for (int i = 0; i < M; i++) {
			if (nbCredit[i] > problem.maxCredit[i]) {
				flag = false;
				System.err.println("Error::TeacherOverload: nbCredit[" + i + "] = " + nbCredit[i] + ", max = "
						+ problem.maxCredit[i]);
			}
		}

		return flag;
	}
	
	public int[] solve() {
		int[] result = new int[problem.numClass];
		this.buildPreProcessModel();
		this.executePreProcess(100, 10, 1000000, 50);

		this.buildOptimizationModel();
		TabuSearch tb = new TabuSearch();
		tb.searchMaintainConstraintsMinimize(obj, S, 100, 60, 1000000, 100);

		for (int i = 0; i < problem.numClass; i++) {
			if (this.exception.contains(i)) {
				result[i] = -1;
			} else {
				result[i] = X[i].getValue();
			}
		}

//		check(result);

		return result;
	}

	public static void main(String[] args) throws FileNotFoundException {
		BCA_Problem testcase = new BCA_Problem();
		Scanner in = new Scanner(new File("D:\\Desktop\\2018.txt"));
		testcase.numClass = in.nextInt();
		testcase.numTeacher = in.nextInt();
		testcase.possibleTeacherForClass = new ArrayList[testcase.numClass];
		testcase.creditOfClass = new int[testcase.numClass];
		for (int i = 0; i < testcase.numClass; i++) {
			testcase.possibleTeacherForClass[i] = new ArrayList<Integer>();
			in.nextInt();
			testcase.creditOfClass[i] = in.nextInt();
			int num = in.nextInt();
			for (int j = 0; j < num; j++) {
				testcase.possibleTeacherForClass[i].add(in.nextInt());
			}
		}

		testcase.maxCredit = new int[testcase.numTeacher];
		for (int i = 0; i < testcase.numTeacher; i++) {
			testcase.maxCredit[in.nextInt()] = in.nextInt();
		}

		int confNo = in.nextInt();
		testcase.conflictPairs = new int[confNo][2];
		for (int i = 0; i < confNo; i++) {
			testcase.conflictPairs[i][0] = in.nextInt();
			testcase.conflictPairs[i][1] = in.nextInt();
		}

		CBLSSolver solver = new CBLSSolver(testcase);
		int result[] = solver.solve();
	}

}
