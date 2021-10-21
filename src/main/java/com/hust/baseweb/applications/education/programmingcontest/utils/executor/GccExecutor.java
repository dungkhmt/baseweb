package com.hust.baseweb.applications.education.programmingcontest.utils.executor;


import com.hust.baseweb.applications.education.programmingcontest.entity.TestCase;

import java.util.List;

public class GccExecutor {
    private static final String buildCmd = "g++ -o main main.cpp";
    private static final String suffixes =".cpp";
    private static final String SHFileStart = "#!/bin/bash\n";

    public GccExecutor(){

    }

    public String generateScriptFileWithTestCaseAndCorrectSolution(String source, String testCase, String tmpName, int timeLimit){

        String sourceSH = SHFileStart
                + "mkdir -p " + tmpName +"\n"
                + "cd " + tmpName +"\n"
                + "cat <<EOF >> main"  + suffixes + "\n"
                + source + "\n"
                + "EOF" + "\n"
                + "cat <<EOF >> testcase.txt \n"
                + testCase +"\n"
                + "EOF" + "\n"
                + buildCmd +"\n"
                + "FILE=main" +"\n"
                +"if test -f \"$FILE\"; then" +"\n"
                + "    cat testcase.txt | timeout " + timeLimit +"s " +"./main && echo -e \"\\nSuccessful\" || echo Time Limit Exceeded" + "\n"
                + "else\n"
                + "  echo Compile Error\n"
                + "fi" + "\n"
                + "cd .. \n"
                + "rm -rf " + tmpName + " & "+"\n"
                + "rm -rf " + tmpName+".sh" + " & "+"\n";

        return sourceSH;
    }

    public String checkCompile(String source, String tmpName){

        String sourceSH = SHFileStart
                + "mkdir -p " + tmpName +"\n"
                + "cd " + tmpName +"\n"
                + "cat <<EOF >> main"  + suffixes + "\n"
                + source + "\n"
                + "EOF" + "\n"
                + buildCmd +"\n"
                + "FILE=main" +"\n"
                +"if test -f \"$FILE\"; then" +"\n"
                + "  echo Successful\n"
                + "else\n"
                + "  echo Compile Error\n"
                + "fi" + "\n"
                + "cd .. \n"
                + "rm -rf " + tmpName + " & "+"\n"
                + "rm -rf " + tmpName+".sh" + " & "+"\n";

        return sourceSH;
    }
    public String genSubmitScriptFile(List<TestCase> testCases, String source, String tmpName, int timeout){
        String genTestCase = "";
        for(int i = 0; i < testCases.size(); i++){
            String testcase = "cat <<EOF >> testcase" + i + ".txt \n"
                            + testCases.get(i).getTestCase() +"\n"
                            +"EOF" + "\n";
            genTestCase += testcase;
        }
        String sourceSH = SHFileStart
                + "mkdir -p " + tmpName +"\n"
                + "cd " + tmpName +"\n"
                + "cat <<EOF >> main"  + suffixes + "\n"
                + source + "\n"
                + "EOF" + "\n"
                + buildCmd +"\n"
                + "FILE=main" +"\n"
                +"if test -f \"$FILE\"; then" +"\n"
                + genTestCase +"\n"
                + "n=0\n"
                + "while [ \"$n\" -lt " + testCases.size()+" ]"+"\n"
                + "do\n"
                + "f=\"testcase\"$n\".txt\"" +"\n"
                + "cat $f | timeout " + timeout + "s" +" ./main" +"\n"
                + "n=`expr $n + 1`\n"
                + "done\n"
                + "else\n"
                + "echo Compile Error\n"
                + "fi" + "\n"
                + "cd .. \n"
                + "rm -rf " + tmpName + " & "+"\n"
                + "rm -rf " + tmpName+".sh" + " & "+"\n";
        return sourceSH;
    }
}
