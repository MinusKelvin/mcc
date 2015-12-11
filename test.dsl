null;
2;
4000000000;
true;
"hullo there";
300.5;
int a = 100;
a;
int b = 200;
b;
func int h() {
	return 5;
}
h;
h();

func string hullo(string thing) {
	return thing;
}
hullo("world");

func func int() number(int i) {
	return new func int() {
		return i;
	};
}
number(5);
number(5)();
func int() d = number(27);
d;
d();
number(33)();
d();

int[] c = new int[] {1,2,3};
c;
c[0];
c[2];
