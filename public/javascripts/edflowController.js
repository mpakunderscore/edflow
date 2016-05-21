edflow

    .controller("edflowController", function ($scope, $rootScope, $http) {

        $rootScope.menuWidth = "50%";
        $rootScope.contentWidth = "50%";
        $rootScope.circleLeft = "calc(50% - 135px)";
        $rootScope.categoriesTop = "calc(50% - 26px)";

        $("#search").keyup(function (e) {
            if (e.keyCode == 13) {
                var category = {};
                category.title = $("#search").val();
                $scope.select(category);
            }
        });

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
    });

