package com.hust.baseweb.applications.education.core.solver;

/*
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
*/

public class OrToolsSolver {
/*
	static {
		// must be absolute path
		System.load("C:\\Program Files\\or-tools\\or-tools_VisualStudio2019-64bit_v7.7.7810\\lib\\jniortools.dll");
	}

	final int bigM = 99999;

	MPSolver solver;
	MPVariable x[][];
	MPVariable f;
	MPVariable[] loadEqualExpValue; // loadEqualExpValue[i] == 1 <- credit_per_teacher[i] == expValue.
	Set<Integer> exception;

	BCA_Problem problem;
	int maxCredit;
	int minCredit;

	public OrToolsSolver(BCA_Problem problem) {
		this.problem = problem;
		this.exception = new HashSet<Integer>();
	}

	public boolean preProcess() {
		solver = new MPSolver("SimpleMipProgram", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
		int M = problem.numTeacher;
		int N = problem.numClass;
		int varCount=0;

		int maxCredit = 0;
		for (int i = 0; i < N; i++) {
			maxCredit += problem.creditOfClass[i];
		}

		x = new MPVariable[M][N];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				x[i][j] = solver.makeIntVar(0, 0,
						varCount++ + "-" + problem.input.getClasses()[j].getCode());
			}
		}

		for (int j = 0; j < N; j++) {
			for (int i = 0; i < problem.possibleTeacherForClass[j].size(); i++) {
				x[problem.possibleTeacherForClass[j].get(i)][j] = solver.makeIntVar(0, 1,
						varCount++ + "-" + problem.input.getClasses()[j].getCode());
			}
		}

		for (int i = 0; i < N; i++) {
			MPConstraint tmp = solver.makeConstraint(0, 1);
			for (int j = 0; j < M; j++) {
				tmp.setCoefficient(x[j][i], 1.0);
			}
		}

		for (int i = 0; i < problem.conflictPairs.length; i++) {
			for (int j = 0; j < M; j++) {
				MPConstraint tmp = solver.makeConstraint(0, 1);
				tmp.setCoefficient(x[j][problem.conflictPairs[i][0]], 1);
				tmp.setCoefficient(x[j][problem.conflictPairs[i][1]], 1);
			}
		}

		MPObjective objective = solver.objective();
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				objective.setCoefficient(x[i][j], 1);
			}
		}

		objective.setMaximization();
		final MPSolver.ResultStatus resultStatus = solver.solve();

		if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
			if (objective.value() == N) {
				return true;
			} else {
				for (int i = 0; i < N; i++) {
					int count = 0, teacher = 0;
					while (count == 0 && teacher < M) {
						if (x[teacher][i].solutionValue() > 0.5) {
							count++; 
						}
						teacher++;
					}
					if (count == 0) {
						this.exception.add(i);
//						System.out.println(
//								"Exception detected, class " + problem.input.getClasses()[i].getCode() + " excluded.");
					}
				}
			}
//			System.out.println("Preprocess done, Objective = " + objective.value());
			return true;
		} else {
//			System.out.println("Preprocess failed.");
			return false;
		}
	}

	public boolean executePhase1() {
		solver = new MPSolver("SimpleMipProgram", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
		int M = problem.numTeacher;
		int N = problem.numClass;
		int varCount = 0;

		int maxCredit = 0;
		for (int i = 0; i < N; i++) {
			maxCredit += problem.creditOfClass[i];
		}
		f = solver.makeIntVar(0, maxCredit, "Objective function");

		x = new MPVariable[M][N];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				x[i][j] = solver.makeIntVar(0, 0,
						varCount++ + "-" + problem.input.getClasses()[j].getCode());
			}
		}

		for (int j = 0; j < N; j++) {
			for (int i = 0; i < problem.possibleTeacherForClass[j].size(); i++) {
				x[problem.possibleTeacherForClass[j].get(i)][j] = solver.makeIntVar(0, 1,
						varCount++ + "-" + problem.input.getClasses()[j].getCode());
			}
		}

		for (int i = 0; i < N; i++) {
			MPConstraint tmp = this.exception.contains(i) ? solver.makeConstraint(0, 0) : solver.makeConstraint(1, 1);
			for (int j = 0; j < M; j++) {
				tmp.setCoefficient(x[j][i], 1.0);
			}
		}

		for (int i = 0; i < problem.conflictPairs.length; i++) {
			for (int j = 0; j < M; j++) {
				MPConstraint tmp = solver.makeConstraint(0, 1);
				tmp.setCoefficient(x[j][problem.conflictPairs[i][0]], 1);
				tmp.setCoefficient(x[j][problem.conflictPairs[i][1]], 1);
			}
		}

		for (int i = 0; i < M; i++) {
			MPConstraint tmp = solver.makeConstraint(0, bigM);
			tmp.setCoefficient(f, 1);
			for (int j = 0; j < N; j++) {
				tmp.setCoefficient(x[i][j], -problem.creditOfClass[j]);
			}
		}

		MPObjective objective = solver.objective();
		objective.setCoefficient(f, 1);
		objective.setMinimization();

		final MPSolver.ResultStatus resultStatus = solver.solve();

		if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
			this.maxCredit = (int) (objective.value() + 0.5);
//			System.out.println("Phase 1 done, Obj = " + this.maxCredit);
			return true;
		} else {
//			System.out.println("Phase 1 failed.");
			return false;
		}
	}

	public boolean executePhase2() {
		solver = new MPSolver("SimpleMipProgram", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
		int M = problem.numTeacher;
		int N = problem.numClass;
		int varCount = 0;

		int maxCredit = 0;
		for (int i = 0; i < N; i++) {
			maxCredit += problem.creditOfClass[i];
		}
		f = solver.makeIntVar(0, maxCredit, "Objective function");

		x = new MPVariable[M][N];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				x[i][j] = solver.makeIntVar(0, 0,
						varCount++ + "-" + problem.input.getClasses()[j].getCode());
			}
		}

		for (int j = 0; j < N; j++) {
			for (int i = 0; i < problem.possibleTeacherForClass[j].size(); i++) {
				x[problem.possibleTeacherForClass[j].get(i)][j] = solver.makeIntVar(0, 1,
						varCount++ + "-" + problem.input.getClasses()[j].getCode());
			}
		}

		for (int i = 0; i < N; i++) {
			MPConstraint tmp = this.exception.contains(i) ? solver.makeConstraint(0, 0) : solver.makeConstraint(1, 1);
			for (int j = 0; j < M; j++) {
				tmp.setCoefficient(x[j][i], 1.0);
			}
		}

		for (int i = 0; i < problem.conflictPairs.length; i++) {
			for (int j = 0; j < M; j++) {
				MPConstraint tmp = solver.makeConstraint(0, 1);
				tmp.setCoefficient(x[j][problem.conflictPairs[i][0]], 1);
				tmp.setCoefficient(x[j][problem.conflictPairs[i][1]], 1);
			}
		}

		for (int i = 0; i < M; i++) {
			MPConstraint tmp = solver.makeConstraint(0, bigM);
			for (int j = 0; j < N; j++) {
				tmp.setCoefficient(x[i][j], problem.creditOfClass[j]);
			}
			tmp.setCoefficient(f, -1);
		}

		MPObjective objective = solver.objective();
		objective.setCoefficient(f, 1);
		objective.setMaximization();

		final MPSolver.ResultStatus resultStatus = solver.solve();

		if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
			this.minCredit = (int) (objective.value() + 0.5);
//			System.out.println("Phase 2 done, Obj = " + this.minCredit);
			return true;
		} else {
//			System.out.println("Phase 2 failed");
			return false;
		}
	}

	public boolean executePhase3(int expValue) {
		int M = problem.numTeacher;
		int N = problem.numClass;
		int varCount = 0;

		int[] optTeachers = new int[M];
		ArrayList[] optClasses = new ArrayList[M];
		for (int i = 0; i < M; i++) {
			optTeachers[i] = 0;
			optClasses[i] = new ArrayList<Integer>();
		}

		while (expValue >= minCredit) {
			solver = new MPSolver("SimpleMipProgram", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

			ArrayList[] possibleClasssOfTeacher = new ArrayList[M];
			for (int i = 0; i < M; i++) {
				possibleClasssOfTeacher[i] = new ArrayList<Integer>();
			}
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < problem.possibleTeacherForClass[i].size(); j++) {
					possibleClasssOfTeacher[problem.possibleTeacherForClass[i].get(j)].add(i);
				}
			}

			x = new MPVariable[M][N];
			for (int i = 0; i < M; i++) {
				for (int j = 0; j < N; j++) {
					x[i][j] = solver.makeIntVar(0, 0, varCount++ + "-" + problem.input.getClasses()[j].getCode());
				}
				if (optTeachers[i] == 1) {
					for (int j = 0; j < optClasses[i].size(); j++) {
						x[i][(int) optClasses[i].get(j)] = solver.makeIntVar(1, 1,
								varCount++ + "-" + problem.input.getClasses()[j].getCode());
					}
				} else {
					for (int j = 0; j < possibleClasssOfTeacher[i].size(); j++) {
						x[i][(int) possibleClasssOfTeacher[i].get(j)] = solver.makeIntVar(0, 1,
								varCount++ + "-" + problem.input.getClasses()[j].getCode());
					}
				}
			}

			for (int i = 0; i < N; i++) {
				MPConstraint tmp = this.exception.contains(i) ? solver.makeConstraint(0, 0)
						: solver.makeConstraint(1, 1);
				for (int j = 0; j < M; j++) {
					tmp.setCoefficient(x[j][i], 1.0);
				}
			}

			for (int i = 0; i < problem.conflictPairs.length; i++) {
				for (int j = 0; j < M; j++) {
					MPConstraint tmp = solver.makeConstraint(0, 1);
					tmp.setCoefficient(x[j][problem.conflictPairs[i][0]], 1);
					tmp.setCoefficient(x[j][problem.conflictPairs[i][1]], 1);
				}
			}

			loadEqualExpValue = new MPVariable[M];
			MPConstraint loadOfTeacher[] = new MPConstraint[M];
			for (int i = 0; i < M; i++) {
				if (optTeachers[i] == 0) {

					loadOfTeacher[i] = solver.makeConstraint(this.minCredit, Math.min(expValue, problem.maxCredit[i]));
					for (int j = 0; j < N; j++) {
						loadOfTeacher[i].setCoefficient(x[i][j], problem.creditOfClass[j]);
					}

					loadEqualExpValue[i] = solver.makeIntVar(0, 1, "" + varCount++);
					MPConstraint c1 = solver.makeConstraint(-bigM, 1 + expValue * bigM);
					c1.setCoefficient(loadEqualExpValue[i], 1);
					for (int j = 0; j < N; j++) {
						c1.setCoefficient(x[i][j], bigM * problem.creditOfClass[j]);
					}

					MPConstraint c2 = solver.makeConstraint(1 - expValue * bigM, bigM);
					c2.setCoefficient(loadEqualExpValue[i], 1);
					for (int j = 0; j < N; j++) {
						c2.setCoefficient(x[i][j], -bigM * problem.creditOfClass[j]);
					}

				} else {
					loadEqualExpValue[i] = solver.makeIntVar(0, 0, "" + varCount++);
				}
			}

			MPObjective obj = solver.objective();
			for (int i = 0; i < M; i++) {
				obj.setCoefficient(loadEqualExpValue[i], 1);
			}
			obj.setMinimization();

			final MPSolver.ResultStatus resultStatus = solver.solve();

			if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
//				System.out.println(obj.value());
				boolean check = false;
				for (int i = 0; i < M; i++) {
					if (optTeachers[i] == 0) {
						check = false;
						int load = 0;
						for (int j=0; j<N; j++) {
							if (x[i][j].solutionValue() > 0.5) {
								load += problem.creditOfClass[j];
							}
						}
						if (load == expValue) {
							optTeachers[i] = 1;
							check = true;
						}
						
						for (int j = 0; j < N; j++) {
							if (x[i][j].solutionValue() > 0.75) {
								if (check) {
									optClasses[i].add(j);
								}
							}
						}
//						if (check) {
//							System.out.println(load + " <= " + problem.maxCredit[i]);
//						}
					}
				}
				expValue--;
				
				// tunning
				int load[] = new int[M];
				for (int i=0; i<M; i++) {
					for (int j=0; j<N; j++) {
						if (x[i][j].solutionValue() > 0.5) {
							load[i] += problem.creditOfClass[j];
						}
					}
				}
				boolean stop = false;
				while (!stop && expValue>=this.minCredit) {
					for (int i=0; i<M; i++) {
						if (load[i] == expValue) {
							stop = true; break;
						}
					}
					if (!stop) expValue--;
				}
			} else {
//				System.out.println("Phase 3: Cannot find optimal solution");
				return false;
			}
		}
		return true;
	}

	public void solve(int[] result) {
		boolean sucess = true;
		if (this.preProcess()) {
			if (this.executePhase1()) {
				if (this.executePhase2()) {
					if (this.executePhase3(this.maxCredit)) {
						for (int i = 0; i < problem.numClass; i++) {
							if (this.exception.contains(i)) {
								result[i] = -1;
							} else {
								for (int j = 0; j < problem.numTeacher; j++) {
									if (x[j][i].solutionValue() > 0.5) {
										result[i] = j;
										break;
									}
								}
							}
						}
					} else {
						sucess = false;
					}
				} else {
					sucess = false;
				}
			} else {
				sucess = false;
			}
		} else {
			sucess = false;
		}
		if (!sucess) {
			for (int i = 0; i < problem.numClass; i++) {
				result[i] = -1;
			}
		}
	}
*/
}

