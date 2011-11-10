
mainClass := geel.WallTracker2
nxtAddress := 00:16:53:02:F7:9D


mainClassFile := $(subst .,/,${mainClass}).java
nxjFile := $(basename $(notdir ${mainClassFile})).nxj
srcDir := src
binDir := antbin


build: 
	nxjc -sourcepath ${srcDir}/ -d ${binDir}/ ${srcDir}/${mainClassFile} -g

link: build
	nxjlink -cp ${binDir}/ -o ${binDir}/${nxjFile} ${mainClass} -v

upload: ${binDir}/${nxjFile}
	nxjupload ${binDir}/${nxjFile} -b -d ${nxtAddress}

run: ${binDir}/${nxjFile}
	nxjupload ${binDir}/${nxjFile} -r -b -d ${nxtAddress}

clean:
	rm -rf ${binDir}/*
