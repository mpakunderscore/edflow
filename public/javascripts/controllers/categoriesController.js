edflow

    .controller("categoriesController", function ($scope, $rootScope, $http, $state) {

        var center = "50%";

        $rootScope.menuWidth = center;
        $rootScope.contentWidth = "calc(100% - " + center + ")";
        $rootScope.circleLeft = "calc(" + center + " - 135px)";
        // $rootScope.categoriesTop = "calc(50% - 26px)";

        var menuItems = [
            {title: "Main"},
            {title: "Flows"},
            {title: "Robot"},
            {title: "Mine"}
        ];

        //TODO
        $rootScope.selectedCategories = [];
        $scope.subCategories = menuItems;
        $scope.categories = [];
        $rootScope.items = [];

        $rootScope.circleAnimation = "";

        $rootScope.selectCategory = function (category) {

            // var path = $state.current.name;

            // console.log("selectCategory: " + path)

            // var category = {title: path};

            // console.log(path);

            if (category !== "") {

                $rootScope.selectedCategories.push(category);
                $rootScope.hideChat();

            } else {

                $rootScope.selectedCategories = [];
                $scope.subCategories = menuItems;

                //?
                // $scope.categories = [];
                $rootScope.items = [];

                $scope.moveCategoriesWidth();
                $rootScope.circleAnimation = "";

                $rootScope.showChat();

                return;
            }
            
            $rootScope.circleAnimation = "animation";

            $scope.subCategories = [];

            $scope.moveCategoriesWidth(category.title);

            var categoryTitle = category.title;

            // if (categoryTitle === "Wikipedia")
            //     categoryTitle = "undefined";

            $http.get("api/" + category.title.toLowerCase() + "?category=" + categoryTitle + "&language=" + $rootScope.language).success(function (data) {

                    $rootScope.items = data.items;
                    // $scope.categories = data.subCategories;

                    $scope.moveCategoriesWidth(category.title);

                    $rootScope.circleAnimation = "";

                }).error(function () {
                    $rootScope.circleAnimation = "error";
                });
        }

        $scope.deselectCategory = function (category) {

            while ($rootScope.selectedCategories.pop() !== category) {
            }

            if ($rootScope.selectedCategories.length == 0)
                $scope.selectCategory("");

            else
                $scope.selectCategory($rootScope.selectedCategories.pop());
        }

        $scope.moveCategoriesWidth = function (path) {

            if ($rootScope.selectedCategories.length === 0) {
                $rootScope.menuWidth = center;
                $rootScope.contentWidth = "calc(100% - " + center + ")";
                $rootScope.circleLeft = "calc(" + center + " - 135px)";

                $scope.moveCategoriesMargin();
                return;
            }

            //TODO
            var longest = $rootScope.getLongest($rootScope.selectedCategories.concat($scope.subCategories.concat($scope.categories)));
            var menuWidth = $rootScope.getTextWidth(longest.title, "16px Open Sans") + 50;
            var percent = menuWidth / document.body.clientWidth * 100;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            if (percent < 20) {
                menuWidth *= 20/percent;
            }

            //console.log(longest.title + " " + menuWidth + " " + percent);

            $rootScope.menuWidth = menuWidth + "px";
            $rootScope.contentWidth = "calc(100% - " + $rootScope.menuWidth + ")";

            if (path !== "words") {
                $rootScope.circleLeft = (menuWidth - 135) + "px";
            }

            $scope.moveCategoriesMargin();
        };

        $scope.moveCategoriesMargin = function () {

            var count = $rootScope.selectedCategories.length + $scope.subCategories.length + $scope.categories.length;;

            var px = 32 * count + 20;
            if ($(window).height() <= px)
                $rootScope.categoriesTop = "0";

            else
                $rootScope.categoriesTop = "calc(50% - " + px/2 + "px)";

        };

        $scope.moveCategoriesMargin();

        $scope.moveContentMargin = function () {
        }
    });