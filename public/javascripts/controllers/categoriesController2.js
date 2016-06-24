edflow

    .controller("categoriesController", function ($scope, $rootScope, $http, $state) {

        var center = "50%";
        var circle = "135px";

        $rootScope.menuWidth = center;
        $rootScope.contentWidth = "calc(100% - " + center + ")";
        $rootScope.circleLeft = "calc(" + center + " - " + circle + ")";
        $rootScope.categoriesTop = "calc(50% - 26px)";

        $rootScope.categories = [
            {title: "Main"},
            {title: "Flows"},
            {title: "Robot"},
            {title: "Mine"}
        ];

        $scope.subCategories = [];

        $rootScope.items = [];

        $rootScope.circleAnimation = "";

        $rootScope.selectCategory = function (category) {

            // console.log(category);
            
            if (category.selected) {

                $rootScope.items = [];
                $rootScope.showChat();
                category.selected = false;
                $scope.moveCategoriesWidth(category);
                return;
            }

            $rootScope.circleAnimation = "animation";

            for (var i = 0, len = $rootScope.categories.length; i < len; i++) {
                $rootScope.categories[i].selected = false;
            }



            // $scope.$apply;

            // $scope.moveCategoriesWidth(category);

            $http.get("api/" + category.title.toLowerCase() + "?language=" + $rootScope.language).success(function (data) {

                category.selected = true;

                $rootScope.hideChat();

                $rootScope.items = data.items;
                $scope.subCategories = data.subCategories;

                $scope.moveCategoriesWidth(category);

                $rootScope.circleAnimation = "";

            }).error(function () {
                $rootScope.circleAnimation = "error";
            });

            // category.selected = true;
        }

        $scope.moveCategoriesWidth = function (category) {

            if (category.selected === false) {
                $rootScope.menuWidth = center;
                $rootScope.contentWidth = "calc(100% - " + center + ")";
                $rootScope.circleLeft = "calc(" + center + " - 135px)";

                $scope.moveCategoriesMargin();
                return;
            }

            //TODO
            var longest = $rootScope.getLongest($rootScope.categories.concat($scope.subCategories));
            var menuWidth = $rootScope.getTextWidth(longest.title, "16px Open Sans") + 50;
            var percent = menuWidth / document.body.clientWidth * 100;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            if (percent < 20) {
                menuWidth *= 20/percent;
            }

            //console.log(longest.title + " " + menuWidth + " " + percent);

            $rootScope.menuWidth = menuWidth + "px";
            $rootScope.contentWidth = "calc(100% - " + $rootScope.menuWidth + ")";
            $rootScope.circleLeft = (menuWidth - 135) + "px";

            $scope.moveCategoriesMargin();
        };

        $scope.moveCategoriesMargin = function () {

            var count = $rootScope.categories.length + $scope.subCategories.length;

            var px = 32 * count + 20;
            if ($(window).height() <= px)
                $rootScope.categoriesTop = "0";

            else
                $rootScope.categoriesTop = "calc(50% - " + px/2 + "px)";

        };

        $scope.moveCategoriesMargin();

    });
