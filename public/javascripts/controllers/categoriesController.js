edflow

    .controller("categoriesController", function ($scope, $rootScope, $http, $state) {

        var menuItems = [
            {title: "News"},
            {title: "Words"},
            {title: "Translate"},
            {title: "Wikipedia"},
            {title: "Book"},
            {title: "Flow"}
        ];

        $rootScope.selectedCategories = [];
        $scope.subCategories = menuItems;
        //$scope.pagesCategories = [];
        $rootScope.items = [];

        $rootScope.circleAnimation = "";

        $rootScope.selectCategory = function (category) {

            console.log(category)
            console.log($state)

            if (category !== "") {

                $rootScope.selectedCategories.push(category);

            } else {

                $rootScope.selectedCategories = [];
                $scope.subCategories = menuItems;
                $rootScope.items = [];

                $scope.moveCategoriesWidth();
                $rootScope.circleAnimation = "";

                return;
            }

            $rootScope.circleAnimation = "animation";

            $scope.subCategories = [];
            $scope.moveCategoriesWidth();

            var categoryTitle = category.title;

            if (categoryTitle === "Wikipedia")
                categoryTitle = "undefined";

            var path = category.title.toLowerCase();

            $http.get("api/" + path + "?category=" + categoryTitle + "&language=" + $rootScope.language).success(function (data) {

                    $rootScope.items = data.items;
                    $scope.subCategories = data.subCategories;

                    $scope.moveCategoriesWidth();

                    $rootScope.circleAnimation = "";
                })
                .error(function () {
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

        $scope.moveCategoriesWidth = function () {

            if ($rootScope.selectedCategories.length === 0) {
                $rootScope.menuWidth = "50%";
                $rootScope.contentWidth = "50%";
                $rootScope.circleLeft = "50% - 135px";

                $scope.moveCategoriesMargin();
                return;
            }

            var longest = $rootScope.getLongest($rootScope.selectedCategories.concat($scope.subCategories));
            var menuWidth = $rootScope.getTextWidth(longest.title, "16px Open Sans") + 50;
            var percent = menuWidth / document.body.clientWidth * 100;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            if (percent < 20)
                menuWidth *= 20/percent;

            //console.log(longest.title + " " + menuWidth + " " + percent);

            $rootScope.menuWidth = menuWidth + "px";
            $rootScope.contentWidth = "calc(100% - " + $rootScope.menuWidth + ")";
            $rootScope.circleLeft = (menuWidth - 135) + "px";

            $scope.moveCategoriesMargin();
        }

        $scope.moveCategoriesMargin = function () {

            var count = $rootScope.selectedCategories.length + $scope.subCategories.length;

            var px = 32 * count + 20;
            if ($(window).height() <= px)
                $rootScope.categoriesTop = "0";

            else
                $rootScope.categoriesTop = "calc(50% - " + px/2 + "px)";

        }

        $scope.moveContentMargin = function () {

        }

        $scope.moveCategoriesMargin();

        console.log($state.get())

        if ($state.current.name !== undefined) {
            //$rootScope.selectCategory({title: $state.current.name});
        }
    })
