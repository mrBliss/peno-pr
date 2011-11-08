
build: 
	nxjc -sourcepath src/ -d bin/ src/Robot.java -g

link: build
	nxjlink -cp bin/ -o bin/Robot.nxj Robot -v

upload: bin/Robot.nxj
	nxjupload bin/Robot.nxj -b -d 00:16:53:06:4E:B1

run: bin/Robot.nxj
	nxjupload bin/Robot.nxj -r -b -d 00:16:53:06:4E:B1
