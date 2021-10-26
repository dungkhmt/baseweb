#!/bin/bash
mkdir -p admin-44515
cd admin-44515
cat <<EOF >> main.cpp
#include <bits/stdc++.h>
using namespace std;

int main(){
  int a,b;
  cin >> a >> b;
  cout << a + b;
  return 0;
}
EOF
g++ -o main main.cpp
FILE=main
if test -f "$FILE"; then
  echo Successful
else
  echo Compile Error
fi
cd .. 
rm -rf admin-44515 & 
rm -rf admin-44515.sh & 
