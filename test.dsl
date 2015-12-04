int a;                                                              // int uninitialized
int b = 0;                                                          // int
int c = 2 + 3;                                                      // int
int d = (34 + 6) * 27;                                              // int
int e = functionCall(56);                                           // int
int f = d;                                                          // int
int g = {int ga = d; int gb = ga * ga + c; gb * gb / ga};           // int
func int h() {26};                                                  // int()
func float i(int ia) {ia / 2.35};                                   // float(int)
func int() j;                                                       // int() uninitialized
func int() k = func int() {26};                                     // int()
func float(int) l = func float(int la) {la / b};                    // float(int)
func int(int, int) m = func int(int ma, int mb) {m(ma,mb-1) * ma};  // int(int,int)
//func int(int) n = func (na) {n(na-1) * n};                          // int(int) [removed since type inheritance will not be implemented]
//struct o;                                                           // struct uninitialized [removed due to uselessness]
struct p {int pa, int pb};                                          // p == struct{int pa, int pb} declaration
struct {int qa, int qb} q;                                          // struct{int qa, int qb}
p r;                                                                // struct{int pa, int pb}
p s = p {pa = 0; pb = 5};                                           // struct{int pa, int pb}
p t = s;                                                            // struct{int pa, int pb}
struct {int pa, int pb} u = s;                                      // struct{int pa, int pb}
p v = {p va = {pa = 0; pb = 0;}; va.pa = s.pb; va};                 // struct{int pa, int pb}
struct w p;                                                         // w == p == struct{int pa, int pb} declaration
int[5] x;                                                           // int[5] uninitialized
int[5] y = [1,2,3,4,5];                                             // int[5]
int[5] z = [0;5];                                                   // int[5]
int[5][2] A = [[11,12,13,14,15],[21,22,23,24,25]];                  // int[5][2]
func void(struct {int Ba, int Bb}) B;                               // void(struct{int Ba, int Bb}) uninitialized
struct n {struct {int na, float nb}, int nc};                       // n == struct{struct{int na, float nb}, int nc} declaration