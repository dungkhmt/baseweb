package com.hust.baseweb.applications.education.programmingcontest.utils.executor;

import com.hust.baseweb.applications.education.programmingcontest.entity.TestCase;

import java.util.List;

public class Python3Executor {
    private static final String suffixes =".py";
    private static final String SHFileStart = "#!/bin/bash\n";
    private static final String buildCmd = "python3 -m py_compile main.py";
    public Python3Executor(){

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
                + "FILE=main.py" +"\n"
                +"if test -f \"$FILE\"; then" +"\n"
                + "    cat testcase.txt | timeout " + timeLimit +"s " +" python3 main.py && echo -e \"\\nnSuccessful\"  || echo Time Limit Exceeded" + "\n"
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
                + "if  [-d __pycache__]; then" +"\n"
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
                + "if  [-d __pycache__]; then" +"\n"
                + genTestCase +"\n"
                + "n=0\n"
                + "while [ \"$n\" -lt " + testCases.size()+" ]"+"\n"
                + "do\n"
                + "f=\"testcase\"$n\".txt\"" +"\n"
                + "cat $f | timeout " + timeout + "s" +" python3 main.py " +"\n"
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
