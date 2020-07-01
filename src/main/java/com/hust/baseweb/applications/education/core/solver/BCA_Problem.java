package com.hust.baseweb.applications.education.core.solver;

import java.util.ArrayList;
import java.util.List;

import com.hust.baseweb.applications.education.core.model.AssignmentInput;
import com.hust.baseweb.applications.education.core.model.Class;
import com.hust.baseweb.applications.education.core.model.Course;
import com.hust.baseweb.applications.education.core.model.Session;
import com.hust.baseweb.applications.education.core.solver.cblssolver.CBLSSolver;
import com.hust.baseweb.applications.education.entity.ClassTeacherCompositeId;
import com.hust.baseweb.applications.education.entity.EduClassTeacherAssignment;

import localsearch.model.LocalSearchManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BCA_Problem {

	public int numClass;
	public int numTeacher;
	AssignmentInput input;

	public ArrayList<Integer>[] possibleTeacherForClass;
	public ArrayList<Integer>[] priority;
	public int[][] conflictPairs;
	public int[] creditOfClass;
	public int[] maxCredit;

	public int[] assignmentResult;

	public BCA_Problem() {
	}

	public BCA_Problem(AssignmentInput input) {
		this.input = input;
		this.numClass = input.getClasses().length;
		this.numTeacher = input.getTeachers().length;
		this.convertConflictPairs(input);
		this.convertCreditOfClass(input);
		this.convertMaxCredit(input);
		this.convertPossibleTeacherForClass(input);

//		System.out.println(this.numClass + " " + this.numTeacher);
//		for (int i = 0; i < this.numClass; i++) {
//			System.out.print(i + " " + this.creditOfClass[i] + " " + this.possibleTeacherForClass[i].size() + " ");
//			for (int j : this.possibleTeacherForClass[i]) {
//				System.out.print(j + " ");
//			}
//			System.out.println();
//		}
//
//		for (int i = 0; i < this.numTeacher; i++) {
//			System.out.println(i + " " + this.maxCredit[i]);
//		}
//
//		System.out.println(this.conflictPairs.length);
//		for (int i = 0; i < this.conflictPairs.length; i++) {
//			System.out.println(this.conflictPairs[i][0] + " " + this.conflictPairs[i][1]);
//		}
	}

	public void solve(List<EduClassTeacherAssignment> result, List<String> exception) {
//		CPLEXSolver solver = new CPLEXSolver(this);
//		OrToolsSolver solver = new OrToolsSolver(this);

		CBLSSolver solver = new CBLSSolver(this);
		assignmentResult = solver.solve();

		for (int i = 0; i < numClass; i++) {
			EduClassTeacherAssignment eduAssignment = new EduClassTeacherAssignment();
			ClassTeacherCompositeId assignment = new ClassTeacherCompositeId();
			assignment.setClassId(input.getClasses()[i].getCode());
			if (assignmentResult[i] >= 0) {
				assignment.setTeacherId(input.getTeachers()[assignmentResult[i]].getCode());
			} else {
				assignment.setTeacherId("NULL");
				exception.add(input.getClasses()[i].getCode());
				
			}
			eduAssignment.setClassTeacherCompositeId(assignment);
			result.add(eduAssignment);
		}

		log.info("BCA_Problem, executing done.");
	}

	private void convertPossibleTeacherForClass(AssignmentInput input) {

		this.possibleTeacherForClass = new ArrayList[this.numClass];
		this.priority = new ArrayList[this.numClass];

		for (int i = 0; i < this.numClass; i++) {

			this.possibleTeacherForClass[i] = new ArrayList<Integer>();
			this.priority[i] = new ArrayList<Integer>();

			for (int j = 0; j < this.numTeacher; j++) {

				for (Course course : input.getTeachers()[j].getCourses()) {

					if (input.getClasses()[i].getCourse().getCode().equals(course.getCode())
							&& input.getClasses()[i].getCourse().getType().equals(course.getType())) {

						this.possibleTeacherForClass[i].add(j);
						this.priority[i].add(1);
						break;
					}
				}
			}
		}
	}

	/**
	 * Generate list of conflict pair
	 *
	 * @param input
	 */
	private void convertConflictPairs(AssignmentInput input) {

		ArrayList<Integer[]> temp = new ArrayList<Integer[]>();

		for (int i = 0; i < this.numClass; i++) {

			for (int j = i + 1; j < this.numClass; j++) {

				if (isConflict(input.getClasses()[i], input.getClasses()[j])) {
					Integer[] pair = new Integer[2];
					pair[0] = i;
					pair[1] = j;
					temp.add(pair);
				}
			}
		}
		this.conflictPairs = new int[temp.size()][2];

		for (int i = 0; i < temp.size(); i++) {
			for (int j = 0; j < 2; j++) {
				this.conflictPairs[i][j] = temp.get(i)[j];
			}
		}
	}

	/**
	 * Check time conflict between class1 and class2
	 */
	private boolean isConflict(Class class1, Class class2) {
		for (Session session1 : class1.getTimeTable()) {
			for (Session session2 : class2.getTimeTable()) {
				if (session1.isConflict(session2)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param input
	 */
	private void convertCreditOfClass(AssignmentInput input) {
		this.creditOfClass = new int[this.numClass];
		for (int i = 0; i < this.numClass; i++) {
			this.creditOfClass[i] = input.getClasses()[i].getCredit();
		}
	}

	private void convertMaxCredit(AssignmentInput input) {
		this.maxCredit = new int[this.numTeacher];
		for (int i = 0; i < this.numTeacher; i++) {
			this.maxCredit[i] = input.getTeachers()[i].getMaxCredit();
		}
	}

}
