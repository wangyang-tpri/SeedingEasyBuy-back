<template>
    <div id="mask">
        <div id="jigsaw">
            <canvas id="can" height="155" width="310"></canvas>
            <canvas class="block" height="155" width="63"></canvas>
            <div class="sliderContainer">
                <span class="sliderText">{{ spanTitle }}</span>
                <div class="sliderMask">
                    <div class="slider">
                        <span class="sliderIcon"></span>
                    </div>
                </div>
            </div>
            <div class="refreshIcon"></div>
        <img :src="image" crossOrigin="Anonymous"  @load="drawImage2" />
        </div>
    </div>
</template>
<script>
import jigsaw from "../utils/slider";
/**
 * 需要解决就是
 *
 * 1. 就是将canvas 和 div元素进行全部固定，不需要动态生成
 * 2. 就是需要动态的变换图片 ？ 就是要解决如何动态生成图片的问题
 *	在vue中图片需要通过import的方法动态的导入打文件中
 *
 *
 *
 *
 * 失败： 1 图形拖动失败 刷新图形
 * 		2.用户名 密码 不正确的话 重新输入
 * 成功： 就是直接进行登录
 */

import drag1 from "../assets/drag_1.jpg";
import drag2 from "../assets/drag_2.jpg";
import drag3 from "../assets/drag_3.jpg";
import drag4 from "../assets/drag_4.jpg";
import drag5 from "../assets/drag_5.jpg";
import drag6 from "../assets/drag_6.jpg";
import drag7 from "../assets/drag_7.jpg";
import drag8 from "../assets/drag_8.jpg";
import drag9 from "../assets/drag_9.jpg";
import drag10 from "../assets/drag_10.jpg";
import drawImage from '../utils/slider';
export default {
    name: "dragImage",
    data() {
        return {
            imageSrcArr: [
                drag1,
                drag2,
                drag3,
                drag4,
                drag5,
                drag6,
                drag7,
                drag8,
                drag9,
                drag10,
            ],
            image: "../assets/drag_1.jpg",
            spanTitle: "向右拖动滑块进行验证",
            block: "",
            canvas: "",
            refreshEle: "",
            el: "",
            sliderEle: "",
            l: 42,
            r: 9,
            PI: Math.PI,
            L: this.l + this.r * 2 + 3,
            sliderContainer: "",
            sliderMask: "",
            height: 155,
            width: 310,
            blo2d: "",
            con2d: "",
        };
    },
    methods: {
        initEle() {
            this.sliderContainer =
                document.getElementsByClassName("sliderContainer")[0];
            this.el = document.getElementById("jigsaw");
            this.image = this.imageSrcArr[jigsaw.getRoundNumber(1, 10)];
            this.block = document.getElementsByClassName("block")[0];
            this.canvas = document.getElementById("can");
            this.refreshEle = document.getElementsByClassName("refreshIcon")[0];
            //创建画笔
            this.con2d = this.canvas.getContext("2d");
            this.blo2d = this.block.getContext("2d");
            this.sliderEle = document.getElementsByClassName("slider")[0];
            this.sliderMask = document.getElementsByClassName("sliderMask")[0];
        },
        bindEvents() {
            var self = this;
            self.el.onselectstart = function () {
                return false;
            };
            self.refreshEle.onclick = function () {
                self.reset();
            };
            var originX,
                originY,
                isMouseDown = false,
                trail = [];
            // 鼠标放到拖放块上面时的初始x y的距离
            var handleStart = function (e) {
                originX = e.clientX || e.touches[0].clientX;
                originY = e.clientY || e.touches[0].clientY;
                isMouseDown = true;
            };
            /**
             * @description 判断鼠标移动的距离
             * @param { number } move_x 鼠标移动的x轴距离
             * @param { number } move_y 鼠标移动的y轴距离
             * @returns
             */
            var judgeDistance = function (move_x, move_y) {
                if (move_x < 0 || move_x + 38 >= self.width) return false;
                self.block.style.left = move_x + "px";
                self.sliderEle.style.left = move_x + "px";
                var blockLeft =
                    ((self.width - 40 - 20) / (self.width - 40)) * move_x;
                self.block.style.left = blockLeft + "px";
                jigsaw.addClass(self.sliderContainer, "sliderContainer_active");
                self.sliderMask.style.width = move_x + "px";
            };
            // 鼠标开始拖动时的x y 的移动距离
            var handleMove = function (e) {
                if (!isMouseDown) return;
                var client_x = e.clientX || e.touches[0].clientX;
                var client_y = e.clientY || e.touches[0].clientY;
                var move_x = client_x - originX;
                var move_y = client_y - originY;
                judgeDistance(move_x, move_y);
                trail.push(move_y);
            };

            var handleOver = function (e) {
                if (!isMouseDown) return false;
                isMouseDown = false;
                var client_x = e.clientX || e.changedTouches[0].clientX;
                if (client_x == originX) return false;
                jigsaw.removeClass(
                    self.sliderContainer,
                    "sliderContainer_active"
                );
                self.trail = trail;
                var spliced, verified;
                spliced = jigsaw.verify(self).spliced;
                verified = jigsaw.verify(self).verified;
                if (spliced) {
                    if (verified) {
                        jigsaw.addClass(
                            self.sliderContainer,
                            "sliderContainer_success"
                        );
                        typeof self.onSuccess === "function" &&
                            self.onSuccess();
                    } else {
                        jigsaw.addClass(
                            self.sliderContainer,
                            "sliderContainer_fail"
                        );
                        typeof self.onFail === "function" && self.onFail();
                    }
                } else {
                    jigsaw.addClass(
                        self.sliderContainer,
                        "sliderContainer_fail"
                    );
                    self.spanTitle = "滑块位置拖动不正确";
                    setTimeout(function () {
                        typeof self.onFail === "function" && self.onFail();
                    }, 500);
                }
            };
            self.sliderEle.addEventListener("mousedown", handleStart);
            document.addEventListener("mouseover", handleMove);
            document.addEventListener("mouseup", handleOver);
            self.sliderEle.addEventListener("touchstart", handleStart);
            document.addEventListener("touchmove", handleMove);
            document.addEventListener("touchend", handleOver);
        },
        reset() {
            this.sliderContainer.className = "sliderContainer";
            this.block.style.left = 0;
            this.sliderEle.style.left = 0;
            this.sliderMask.style.width = 0;
            this.clean();
            this.image = this.imageSrcArr[jigsaw.getRoundNumber(1, 10)];
        },
        clean() {
            this.blo2d.clearRect(0, 0, 310, 155);
            this.con2d.clearRect(0, 0, 310, 155);
            this.block.width = 63;
        },
        drawImage2() {
			var img = document.getElementsByTagName('img')[0];
			console.log(img)
            let self = this;
            jigsaw.draw(self);
            self.con2d.drawImage(img, 0, 0, self.width, self.height);
            self.blo2d.drawImage(img, 0, 0, self.width, self.height);
            var y = self.y - self.r * 2 - 1;
            var imageData = self.blo2d.getImageData(
                self.x - 3,
                y,
                self.L,
                self.L
            );
            self.block.width = self.L;
            self.blo2d.putImageData(imageData, 0, y);
        },
    },
    mounted() {
		this.el = document.getElementById('jigsaw');
		drawImage.init({
			el: this.el
		})
        // this.initEle();
        // this.bindEvents();
    },
};
</script>

<style>
div#jigsaw {
    width: 310px;
    height: 200px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -50px;
    margin-left: -150px;
    border: 1px #ddd solid;
    background: #ddd;
    z-index: 310;
    border-radius: 3px;
    display: block;
}
div#mask {
    width: 100%;
    height: 100%;
    z-index: 300;
    background: rgba(0, 0, 0, 0.5);
    position: absolute;
    top: 0;
}

.block {
    position: absolute;
    left: 0;
    top: 0;
}

.sliderContainer {
    position: relative;
    text-align: center;
    width: 310px;
    height: 40px;
    line-height: 40px;
    /* margin-top: 15px; */
    background: #f7f9fa;
    color: #45494c;
    border: 1px solid #e4e7eb;
}

.sliderContainer_active .slider {
    height: 38px;
    border: 1px solid #1991fa;
}

.sliderContainer_active .sliderMask {
    height: 38px;
    border-width: 1px;
}

.sliderContainer_success .slider {
    height: 38px;
    top: -1px;
    border: 1px solid #52ccba;
    background-color: #52ccba !important;
}

.sliderContainer_success .sliderMask {
    height: 38px;
    background-color: #d2f4ef;
}

.sliderContainer_success .sliderIcon {
    background-position: 0 0 !important;
}

.sliderContainer_fail .slider {
    height: 38px;
    top: -1px;
    border: 1px solid #f57a7a;
    background-color: #f57a7a !important;
}

.sliderContainer_fail .sliderMask {
    height: 38px;
    border: 1px solid #f57a7a;
    background-color: #fce1e1;
}

.sliderContainer_fail .sliderIcon {
    top: 14px;
    background-position: 0 -82px !important;
}
.sliderContainer_active .sliderText,
.sliderContainer_success .sliderText {
    display: none;
}

.sliderMask {
    position: absolute;
    left: 0;
    top: 0;
    height: 40px;
    background: #d1e9fe;
}

.slider {
    position: absolute;
    top: 0;
    left: 0;
    width: 40px;
    height: 39px;
    background: #fff;
    box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
    cursor: pointer;
    transition: background 0.2s linear;
}

.slider:hover {
    background: #1991fa;
}

.slider:hover .sliderIcon {
    background-position: 0 -13px;
}

.sliderIcon {
    position: absolute;
    top: 15px;
    left: 13px;
    width: 14px;
    height: 12px;
    background: url("../assets/icon_right.png") 0 -26px;
    background-size: 34px 471px;
}

.refreshIcon {
    position: absolute;
    right: 0;
    top: 0;
    width: 34px;
    height: 34px;
    cursor: pointer;
    background: url("../assets/icon_right.png") 0 -437px;
    background-color: green;
    color: black;
    background-size: 34px 471px;
	z-index: 330;
}
img {
    height: 155px;
    width: 310px;
    position: absolute;
    top: 50%;
    left: 50%;
    z-index: 310;
    margin-top: -101.5px;
    margin-left: -155px;
	display: none;
}
</style>