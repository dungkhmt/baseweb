package com.hust.baseweb.applications.education.core.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CPLEXSolver {

	IloCplex solver;
	IloIntVar[][] x;
	IloNumVar f;
	IloIntVar[] loadEqualExpValue; // loadEqualExpValue[i] == 1 <- credit_per_teacher[i] == expValue.
	Set<Integer> exception;

	BCA_Problem problem;
	int maxCredit;
	int minCredit;

	public CPLEXSolver(BCA_Problem problem) {
		this.problem = problem;
		this.exception = new HashSet<Integer>();
	}

	@SuppressWarnings("unchecked")
	public void executePhase3(int expValue) {

		int M = problem.numTeacher;
		int N = problem.numClass;

		int[] optTeachers = new int[M]; // optTeachers[i] = 1: teacher i optimized.
										// = 0: otherwise.
		@SuppressWarnings("rawtypes")
		ArrayList[] optClasses = new ArrayList[M]; // optClasses[i]: list of classes that teachers i assigned in optimal
													// solution.

		for (int i = 0; i < M; i++) {
			optTeachers[i] = 0;
			optClasses[i] = new ArrayList<Integer>();
		}

		while (expValue >= minCredit) {

			x = new IloIntVar[M][N];

			@SuppressWarnings("rawtypes")
			ArrayList[] possibleClasssOfTeacher = new ArrayList[M];
			for (int i = 0; i < M; i++) {
				possibleClasssOfTeacher[i] = new ArrayList<Integer>();
			}
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < problem.possibleTeacherForClass[i].size(); j++) {
					possibleClasssOfTeacher[problem.possibleTeacherForClass[i].get(j)].add(i);
				}
			}

			try {
				solver = new IloCplex();

				for (int i = 0; i < M; i++) {
					for (int j = 0; j < N; j++) {
						x[i][j] = solver.intVar(0, 0, problem.input.getClasses()[j].getCode());
					}
					if (optTeachers[i] == 1) {
						for (int j = 0; j < optClasses[i].size(); j++) {
							x[i][(int) optClasses[i].get(j)] = solver.intVar(1, 1,
									problem.input.getClasses()[j].getCode());
						}
					} else {
						for (int j = 0; j < possibleClasssOfTeacher[i].size(); j++) {
							x[i][(int) possibleClasssOfTeacher[i].get(j)] = solver.intVar(0, 1,
									problem.input.getClasses()[j].getCode());
						}
					}
				}

				IloLinearNumExpr[] flow_in = new IloLinearNumExpr[N];
				for (int i = 0; i < N; i++) {
					flow_in[i] = solver.linearNumExpr();

					for (int j = 0; j < M; j++) {
						flow_in[i].addTerm(1, x[j][i]);
					}

					if (this.exception.contains(i)) {
						solver.addEq(flow_in[i], 0).setName(problem.input.getClasses()[i].getCode());
					} else {
						solver.addEq(flow_in[i], 1).setName(problem.input.getClasses()[i].getCode());
					}
				}

				for (int i = 0; i < problem.conflictPairs.length; i++) {
					for (int j = 0; j < M; j++) {
						solver.addLe(
								solver.sum(solver.prod(1, x[j][problem.conflictPairs[i][0]]),
										solver.prod(1, x[j][problem.conflictPairs[i][1]])),
								1, "conflict" + i + "   " + j);
					}
				}

				IloLinearNumExpr[] credit_per_teacher = new IloLinearNumExpr[M];
				int bigConst = 10000;
				loadEqualExpValue = new IloIntVar[M];

				for (int i = 0; i < M; i++) {

					if (optTeachers[i] == 0) {
						loadEqualExpValue[i] = solver.intVar(0, 1);
						credit_per_teacher[i] = solver.linearNumExpr();

						for (int j = 0; j < N; j++) {
							credit_per_teacher[i].addTerm(problem.creditOfClass[j] * 1.0, x[i][j]);
						}

						solver.addLe(credit_per_teacher[i], expValue);
						solver.addGe(credit_per_teacher[i], this.minCredit);
						solver.addLe(credit_per_teacher[i], problem.maxCredit[i]);

						solver.addLe(solver.sum(solver.prod(1, loadEqualExpValue[i]),
								solver.prod(bigConst, credit_per_teacher[i])), 1 + expValue * bigConst);

						solver.addGe(solver.sum(solver.prod(1, loadEqualExpValue[i]),
								solver.prod(-bigConst, credit_per_teacher[i])), 1 - expValue * bigConst);

					} else {
						loadEqualExpValue[i] = solver.intVar(0, 0);
					}
				}

				solver.addMinimize(solver.sum(loadEqualExpValue));

				solver.setOut(null);
				solver.solve();

				boolean check = false;
				for (int i = 0; i < M; i++) {
					if (optTeachers[i] == 0) {
						check = false;
						if ((int) (solver.getValue(credit_per_teacher[i]) + 0.25) == expValue) {
							optTeachers[i] = 1;
							check = true;
						}

						for (int j = 0; j < N; j++) {
							if (solver.getValue(x[i][j]) > 0.75) {
								if (check) {
									optClasses[i].add(j);
								}
							}
						}
					}
				}

				if (expValue > minCredit) {
					solver.end();
				}
				expValue--;
			} catch (IloException e) {
				e.printStackTrace();
			}
		}
	}

	public void executePhase2() {
		int M = problem.numTeacher;
		int N = problem.numClass;

		x = new IloIntVar[M][N];

		try {
			solver = new IloCplex();

			int maxCredit = 0;
			for (int i = 0; i < N; i++) {
				maxCredit += problem.creditOfClass[i];
			}
			f = solver.intVar(0, maxCredit);

			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					x[i][j] = solver.intVar(0, 0, problem.input.getClasses()[j].getCode());
				}
			}

			for (int j = 0; j < N; j++) {
				for (int i = 0; i < problem.possibleTeacherForClass[j].size(); i++) {
					x[problem.possibleTeacherForClass[j].get(i)][j] = solver.intVar(0, 1,
							problem.input.getClasses()[j].getCode());
				}
			}

			IloLinearNumExpr[] flow_in = new IloLinearNumExpr[N];
			for (int i = 0; i < N; i++) {
				flow_in[i] = solver.linearNumExpr();

				for (int j = 0; j < M; j++) {
					flow_in[i].addTerm(1, x[j][i]);
				}

				if (this.exception.contains(i)) {
					solver.addEq(flow_in[i], 0).setName(problem.input.getClasses()[i].getCode());
				} else {
					solver.addEq(flow_in[i], 1).setName(problem.input.getClasses()[i].getCode());
				}
			}

			for (int i = 0; i < problem.conflictPairs.length; i++) {
				for (int j = 0; j < M; j++) {
					solver.addLe(solver.sum(solver.prod(1, x[j][problem.conflictPairs[i][0]]),
							solver.prod(1, x[j][problem.conflictPairs[i][1]])), 1);
				}
			}

			IloLinearNumExpr[] credit_per_teacher = new IloLinearNumExpr[M];

			for (int i = 0; i < M; i++) {

				credit_per_teacher[i] = solver.linearNumExpr();

				for (int j = 0; j < N; j++) {
					credit_per_teacher[i].addTerm(problem.creditOfClass[j] * 1.0, x[i][j]);
				}
				solver.addLe(credit_per_teacher[i], problem.maxCredit[i]);
				solver.addLe(credit_per_teacher[i], this.maxCredit);
				solver.addGe(credit_per_teacher[i], f);
			}

			solver.addMaximize(f);

			solver.setOut(null);
			solver.solve();

			this.minCredit = (int) (solver.getValue(f) + 0.25);

		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public void executePhase1() {
		int M = problem.numTeacher;
		int N = problem.numClass;

		x = new IloIntVar[M][N];

		try {
			solver = new IloCplex();
			int maxCredit = 0;
			for (int i = 0; i < N; i++) {
				maxCredit += problem.creditOfClass[i];
			}

			f = solver.intVar(0, maxCredit);

			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					x[i][j] = solver.intVar(0, 0, problem.input.getClasses()[j].getCode());
				}
			}

			for (int j = 0; j < N; j++) {
				for (int i = 0; i < problem.possibleTeacherForClass[j].size(); i++) {
					x[problem.possibleTeacherForClass[j].get(i)][j] = solver.intVar(0, 1,
							problem.input.getClasses()[j].getCode());
				}
			}

			IloLinearNumExpr[] flow_in = new IloLinearNumExpr[N];
			for (int i = 0; i < N; i++) {
				flow_in[i] = solver.linearNumExpr();

				for (int j = 0; j < M; j++) {
					flow_in[i].addTerm(1, x[j][i]);
				}

				if (this.exception.contains(i)) {
					solver.addEq(flow_in[i], 0).setName(problem.input.getClasses()[i].getCode());
				} else {
					solver.addEq(flow_in[i], 1).setName(problem.input.getClasses()[i].getCode());
				}
			}

			for (int i = 0; i < problem.conflictPairs.length; i++) {
				for (int j = 0; j < M; j++) {
					solver.addLe(solver.sum(solver.prod(1, x[j][problem.conflictPairs[i][0]]),
							solver.prod(1, x[j][problem.conflictPairs[i][1]])), 1);
				}
			}

			IloLinearNumExpr[] credit_per_teacher = new IloLinearNumExpr[M];

			for (int i = 0; i < M; i++) {

				credit_per_teacher[i] = solver.linearNumExpr();

				for (int j = 0; j < N; j++) {
					credit_per_teacher[i].addTerm(problem.creditOfClass[j] * 1.0, x[i][j]);
				}

				solver.addLe(credit_per_teacher[i], f);
				solver.addLe(credit_per_teacher[i], problem.maxCredit[i]);

			}

			solver.addMinimize(f);

			solver.setOut(null);
			solver.solve();

			this.maxCredit = (int) (solver.getValue(f) + 0.25);

		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public void preProcess() {
		int M = problem.numTeacher;
		int N = problem.numClass;

		x = new IloIntVar[M][N];

		try {
			solver = new IloCplex();

			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					x[i][j] = solver.intVar(0, 0, problem.input.getClasses()[j].getCode());
				}
			}

			for (int j = 0; j < N; j++) {
				for (int i = 0; i < problem.possibleTeacherForClass[j].size(); i++) {
					x[problem.possibleTeacherForClass[j].get(i)][j] = solver.intVar(0, 1,
							problem.input.getClasses()[j].getCode());
				}
			}

			IloLinearIntExpr obj = solver.linearIntExpr();
			IloLinearNumExpr[] flow_in = new IloLinearNumExpr[N];
			for (int i = 0; i < N; i++) {
				flow_in[i] = solver.linearNumExpr();

				for (int j = 0; j < M; j++) {
					flow_in[i].addTerm(1, x[j][i]);
					obj.addTerm(1, x[j][i]);
				}

				solver.addLe(flow_in[i], 1).setName(problem.input.getClasses()[i].getCode());
			}

			for (int i = 0; i < problem.conflictPairs.length; i++) {
				for (int j = 0; j < M; j++) {
					solver.addLe(solver.sum(solver.prod(1, x[j][problem.conflictPairs[i][0]]),
							solver.prod(1, x[j][problem.conflictPairs[i][1]])), 1);
				}
			}
			
			IloLinearNumExpr[] credit_per_teacher = new IloLinearNumExpr[M];
			for (int i = 0; i < M; i++) {
				credit_per_teacher[i] = solver.linearNumExpr();
				for (int j = 0; j < N; j++) {
					credit_per_teacher[i].addTerm(problem.creditOfClass[j] * 1.0, x[i][j]);
				}
				solver.addLe(credit_per_teacher[i], problem.maxCredit[i]);

			}
			
			solver.addMaximize(obj);

			solver.setOut(null);

			if (solver.solve()) {
				if (solver.getObjValue() == N) {
					solver.end();
					return;
				} else {
					String logInfo = "BCASolver-preprocessing, class ";
					for (int i = 0; i < N; i++) {
						if (solver.getValue(flow_in[i]) < 0.5) {
							this.exception.add(i);
							logInfo += problem.input.getClasses()[i].getCode() + " ";
						}
					}
					log.info(logInfo);
				}
			}

			solver.end();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public void solve(int[] result) {
		this.preProcess();
		this.executePhase1();
		this.executePhase2();
		this.executePhase3(this.maxCredit);

		try {
			for (int i = 0; i < problem.numClass; i++) {
				if (this.exception.contains(i)) {
					result[i] = -1;
				} else {
					for (int j = 0; j < problem.numTeacher; j++) {
						if (solver.getValue(x[j][i]) > 0.5) {
							result[i] = j;
							break;
						}
					}
				}
			}
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
}
