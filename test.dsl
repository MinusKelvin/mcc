0;
a;
6*5;
0*a;
b*2;
a*b;
(0);
(a);
(6*5);
(6)*5;
(6*a);
(6)*(a);
0*(a*b);
(3*a)*b;
-2;
-a;
-(16*23);
-3*-6;
a/b;
a%b;
a+b;
a-b;
a+(b+c);
1+3*6;
6*4+5*3;
a+b+c;
0*1*2;
0*a*6;
1/2/3;
a=b=c=0;
!c;
~b;
-a;

a + b;
b;
b = a + b;
(a + b) + a;
{
	a+=b;
	return a;
}
[1,2,3,4,5];
[];
[1];
[1;10];
[[1,2,3,4],[1,2,3,4,5],[]];
if (true) {
	blah;
}
if (true) blah;
if (true) {
	blah;
} else {
	blah;
}
if (true) blah;
else blah;
;
if (a)
	1;
else if (b)
	2;
else
	3;

if (a)
	if (b)
		;
	else
		;

while (true) {
	blah;
}
while (true)
	blah;
for (a=0; a < 3; a++) {
}
for (a=0; a < 3; a++)
	;

a++;
++a;
b--;
--b;

do {
	;
} while (x);
do; while(x);

a();
a(b);
a(b,c);
a()();

a.b;
a.b.c;
a.b.c++;
++a.b.c;

a.b.c();
a.b().c.d();

a().b;

a[1];
a[1][1];

a.b[1];

a()[1].a;

func int fib(int n) {
	if (n == 1)
		return 1;
	else if (n == 2)
		return 1;
	else
		return fib(n-1) + fib(n-2);
}

func func int() h() {
	return new func int() {return 0;};
}

static const int zed = 'Z';
const static int zee = 'Z';
const int ze = 'z';
static int z = 'z';

int a;                                                                         // int uninitialized
int b = 0;                                                                     // int
int c = 2 + 3;                                                                 // int
int d = (34 + 6) * 27;                                                         // int
int e = functionCall(56);                                                      // int
int f = d;                                                                     // int
func int() j;                                                                  // int() uninitialized
func int h() {return 26;}                                                      // int()
func float i(int ia) {return ia / 2.35;}                                       // float(int)
func int() k = new func int() {return 26;};                                    // int()
func float(int) l = new func float(int la) {return la / b;};                   // float(int)
func int(int, int) m = new func int(int ma, int mb) {return m(ma,mb-1) * ma;}; // int(int,int)
struct{int qa, int qb} q;                                                      // struct{int qa, int qb}
typedef p = struct{int pa, int pb};                                            // p == struct{int pa, int pb} declaration
p r = new p {pa = 0, pb = 5};                                                  // struct{int pa, int pb}
p t = s;                                                                       // struct{int pa, int pb}
struct {int pa, int pb} u = s;                                                 // struct{int pa, int pb}
typedef w = p;                                                                 // w == p == struct{int pa, int pb} declaration
int[] x;                                                                       // int[] uninitialized
int[] y = [1,2,3,4,5];                                                         // int[]
int[] z = [0;5];                                                               // int[]
int[][] A = [[11,12,13,14,15],[21,22,23,24,25]];                               // int[][]
func void(struct {int Ba, int Bb}) B;                                          // void(struct{int Ba, int Bb}) uninitialized
typedef n = struct {struct {int na, float nb} nc, int nd};                     // n == struct{struct{int na, float nb} nc, int nd} declaration

new struct{func int[](float[]) several} {several = new func int[](float[] i) {return [0;i.length];}}.pa[0];