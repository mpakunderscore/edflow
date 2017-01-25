edflow

    .controller("edflowController", function ($scope, $rootScope, $http) {

        //TODO Utils

        $rootScope.clicks = 0;

        $rootScope.circleSweep = false;

        $rootScope.getLongest = function (arr) {

            var length = 0;
            var longest;
            for (var i = 0; i < arr.length; i++){

                if (arr[i].title.length > length) {

                    length = arr[i].title.length;
                    longest = arr[i];
                }
            }

            return longest;
        };

        $rootScope.getTextWidth = function (text, font) {

            // re-use canvas object for better performance
            var canvas = $rootScope.getTextWidth.canvas || ($rootScope.getTextWidth.canvas = document.createElement("canvas"));
            var context = canvas.getContext("2d");
            context.font = font;
            var metrics = context.measureText(text);
            return metrics.width;
        };

        $rootScope.circle = function () {

            $rootScope.clicks++;

            if ($rootScope.clicks == 3)
                $rootScope.send("Stop it.")

            if ($rootScope.clicks == 8)
                $rootScope.send("Seriously..")


            if (!$rootScope.circleSweep) {
                document.querySelector("#circle").style.background = "rgba(255, 255, 255, 0.9)";
                document.querySelector("html").style.background = "#37b9ff";
            } else {
                document.querySelector("#circle").style.background = "#37b9ff";
                document.querySelector("html").style.background = "rgba(255, 255, 255, 0.9)";
            }

            $rootScope.circleSweep = !$rootScope.circleSweep;
        }
    });

