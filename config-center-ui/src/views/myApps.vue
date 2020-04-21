<template>
  <div class="wrapper">
    <div class="header">
      <div class="description">当前应用</div>
      <el-select v-model="currApp" placeholder="请选择应用" size="medium">
        <el-option v-for="item in apps" :key="item.appId" :label="item.appName" :value="item.appId"></el-option>
      </el-select>
      <div class="permission">{{currApp.permission}}</div>
    </div>
    <div class="content">
      <div style="display:flex; justify-content: flex-end; padding: 20px 0 5px">
        <el-button type="primary">新增配置</el-button>
      </div>
      <el-table
        v-loading="listLoading"
        :data="tableList"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column align="center" label="配置key" width="180">
          <template slot-scope="{row}">
            <span>{{ row.key }}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="300px" label="配置value">
          <template slot-scope="{row}">
            <template v-if="row.edit">
              <el-input v-model="row.value" class="edit-input" size="small" />
              <el-button
                class="cancel-btn"
                size="small"
                icon="el-icon-refresh"
                type="warning"
                @click="cancelEdit(row)"
              >取消</el-button>
            </template>
            <span v-else>{{ row.value }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" width="120">
          <template slot-scope="{row}">
            <el-button
              v-if="row.edit"
              type="success"
              size="small"
              icon="el-icon-circle-check-outline"
              @click="confirmEdit(row)"
            >确定</el-button>
            <el-button
              v-else
              type="primary"
              size="small"
              icon="el-icon-edit"
              @click="row.edit=!!!row.edit"
            >编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getApps } from "@/api/user";
import { listConfig } from "@/api/config";
export default {
  name: "myApp",
  data() {
    return {
      apps: [],
      currApp: {},
      tableList: [],
      listLoading: true
    };
  },
  created() {
    getApps().then(res => {
      this.apps = res.data;
      this.currApp = this.apps[0];
      this.getCfgList();
    });
  },
  methods: {
    async getCfgList() {
      if (!this.currApp.appId) return;
      this.listLoading = true;
      const { data } = await listConfig({ appId: this.currApp.appId });
      const cfgs = [];
      for (let key in data) {
        cfgs.push({ key, value: data[key], originValue: data[key] });
        // originValue will be used when user click the cancel
      }
      this.tableList = cfgs;
      this.listLoading = false;
    },
    cancelEdit(row) {
      row.value = row.originValue;
      row.edit = false;
      console.log(row.edit, row.value);
      this.$message({
        message: "The title has been restored to the original value",
        type: "warning"
      });
    },
    confirmEdit(row) {
      row.edit = false;
      console.log(row.edit, row.value);
      row.originValue = row.value;
      this.$message({
        message: "The title has been edited",
        type: "success"
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.wrapper {
  padding: 20px;
  .header {
    display: flex;
    align-items: center;
    height: 47px;
    .description {
      color: rgba(0, 0, 0, 0.8);
      padding-right: 20px;
    }
    .permission {
      color: rgba(0, 0, 0, 0.5);
      text-align: left;
      padding-left: 20px;
    }
  }
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
}
</style>
